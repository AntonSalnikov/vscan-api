package com.dataslab.vscan.service.file;

import com.dataslab.vscan.dto.ValidationStatus;
import com.dataslab.vscan.infra.dynamodb.FileScanResultEntity;
import com.dataslab.vscan.service.domain.FileUploadResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static com.dataslab.vscan.service.file.TempFileUtils.deleteFile;

@Service
@Slf4j
@AllArgsConstructor
class FileServiceImpl implements FileService {

    private final FileStoragePort fileStoragePort;
    private final FileScanResultRepository scanResultRepository;
    private final MailPort mailPort;

    @Override
    public FileUploadResult uploadFile(@NonNull File file, String originalFileName) {
        log.info("Uploading file {} with original name {}", file, originalFileName);

        var mayBePresent = checkIfPresent(file);

        if(mayBePresent.isPresent()) {
            log.info("The same file result {} is already present. Skipping further processing", mayBePresent);
            return mayBePresent.get();
        }

        UUID key = UUID.randomUUID();
        var checksum = fileStoragePort.uploadFile(key, file, originalFileName);
        var entity = scanResultRepository.save(FileScanResultEntity.createNew(key, file.getName(), file.length(), checksum));

        log.info("Successfully processed {}", entity);
        return new FileUploadResult(key, checksum);
    }

    @Override
    public Optional<FileUploadResult> getById(@NonNull UUID id) {
        log.info("Retrieving result by id '{}'", id);
        return scanResultRepository.getById(id)
                .map(s -> new FileUploadResult(s.getId(), s.getValidationStatus(), s.getSha256Hash()));
    }

    @Override
    public Optional<FileUploadResult> getBySha256Hash(@NonNull String hash) {
        log.info("Retrieving result by hash '{}'", hash);
        return scanResultRepository.getByHash(hash)
                .map(s -> new FileUploadResult(s.getId(), s.getValidationStatus(), s.getSha256Hash()));
    }

    @Override
    public void sendToScan(@NonNull String bucket, @NonNull UUID key) {
        log.info("Sending file with id '{}' from bucket '{}' to scan", key, bucket);

        File file = null;
        try {
            var entity = scanResultRepository.getById(key)
                    .orElseThrow(() -> new IllegalStateException("No entity is found with id %s".formatted(key)));

            file = fileStoragePort.downloadFile(bucket, key.toString());
            //mailPort.sendFile(key, file);

            entity.setValidationStatus(ValidationStatus.PROCESSING);
            entity.setModifiedAt(Instant.now());
            scanResultRepository.save(entity);
        } finally {
            deleteFile(file);
        }
    }

    private Optional<FileUploadResult> checkIfPresent(File file) {
        return scanResultRepository.getByHash(calculateSha256(file))
                .map(FileScanResultEntity::to);
    }

    private String calculateSha256(File file) {

        try(var is = new FileInputStream(file)) {
            return Base64.getEncoder().encodeToString(DigestUtils.sha256(is));
        } catch (IOException ioe) {
            log.error("Error appeared while calculating sha256 hash for file {}", file, ioe);
            throw new IllegalStateException("Failed to calculate sha256 hash");
        }
    }
}
