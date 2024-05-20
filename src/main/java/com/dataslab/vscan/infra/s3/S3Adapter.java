package com.dataslab.vscan.infra.s3;

import com.dataslab.vscan.config.aws.S3BucketProperties;
import com.dataslab.vscan.exception.FileUploadException;
import com.dataslab.vscan.service.file.FileStoragePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Adapter implements FileStoragePort {

    private static final long UPLOAD_TIMEOUT_SEC = 10L; //TODO: move to configuration
    private static final String ERROR_MESSAGE = "Error appeared while uploading file.";
    private static final String ORIGINAL_FILE_NAME = "originalFileName";
    private static final String DEFAULT_NAME = "NOT_SPECIFIED";

    private final S3TransferManager s3TransferManager;
    private final S3BucketProperties s3BucketProperties;

    @Override
    public String uploadFile(@NonNull UUID key, @NonNull File file, String originalFileName) {
        log.debug("loading object with key {} to bucket {}", key, s3BucketProperties.getDirtyBucket());

        var putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BucketProperties.getDirtyBucket())
                .checksumAlgorithm(ChecksumAlgorithm.SHA256)
                .metadata(Map.of(ORIGINAL_FILE_NAME, StringUtils.defaultIfBlank(originalFileName, DEFAULT_NAME)))
                .key(key.toString())
                .build();

        var body = AsyncRequestBody.fromFile(file);

        var request = UploadRequest.builder()
                .putObjectRequest(putObjectRequest)
                .requestBody(body)
                .build();

        try {
            var result = s3TransferManager.upload(request).completionFuture().get(UPLOAD_TIMEOUT_SEC, TimeUnit.SECONDS);
            var checksum = result.response().checksumSHA256();

            log.debug("File is successfully loaded. Checksum received: {}", checksum);
            return checksum;

        } catch (InterruptedException e) {
            log.error(ERROR_MESSAGE, e);
            Thread.currentThread().interrupt();
            throw new FileUploadException();
        } catch (ExecutionException | TimeoutException e) {
            log.error(ERROR_MESSAGE, e);
            throw new FileUploadException();
        }
    }
}
