package com.dataslab.vscan.service.file;

import com.dataslab.vscan.infra.dynamodb.FileScanResultEntity;
import com.dataslab.vscan.service.domain.FileUploadResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
class FileServiceImpl implements FileService {

    private final FileStoragePort fileStoragePort;
    private final FileScanResultRepository scanResultRepository;


    @Override
    public FileUploadResult uploadFile(@NonNull File file) {
        log.info("Uploading file {}", file);

        UUID key = UUID.randomUUID();
        var checksum = fileStoragePort.uploadFile(key, file);

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
}
