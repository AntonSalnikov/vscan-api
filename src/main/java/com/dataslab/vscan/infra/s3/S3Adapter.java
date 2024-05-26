package com.dataslab.vscan.infra.s3;

import com.dataslab.vscan.config.aws.S3BucketProperties;
import com.dataslab.vscan.exception.FileDownloadException;
import com.dataslab.vscan.exception.FileUploadException;
import com.dataslab.vscan.service.file.FileStoragePort;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.dataslab.vscan.service.file.TempFileUtils.createTempFile;

@Component
@Slf4j
public class S3Adapter implements FileStoragePort {

    private static final long TIMEOUT_SEC = 10L; //TODO: move to configuration
    private static final String ERROR_UPLOADING_MESSAGE = "Error appeared while uploading file.";
    private static final String ERROR_DOWNLOADING_MESSAGE = "Error appeared while downloading file.";
    private static final String ORIGINAL_FILE_NAME = "originalFileName";
    private static final String DEFAULT_NAME = "NOT_SPECIFIED";

    private final S3TransferManager s3TransferManager;
    private final S3BucketProperties s3BucketProperties;
    private final Path temporaryDirectory;


    public S3Adapter(S3TransferManager s3TransferManager, S3BucketProperties s3BucketProperties, Path temporaryDirectory) {
        log.info("Creating S3Adapter with {}", s3BucketProperties);

        this.s3TransferManager = s3TransferManager;
        this.s3BucketProperties = s3BucketProperties;
        this.temporaryDirectory = temporaryDirectory;
    }


    @Override
    public String uploadFile(@NonNull UUID key, @NonNull File file, String originalFileName) {
        log.debug("loading object with key {} to bucket {}", key, s3BucketProperties.getDirtyBucket());

        var body = AsyncRequestBody.fromFile(file);
        var request = UploadRequest.builder()
                .putObjectRequest(requestBuilder ->
                        requestBuilder.bucket(s3BucketProperties.getDirtyBucket())
                                .checksumAlgorithm(ChecksumAlgorithm.SHA256)
                                .metadata(Map.of(ORIGINAL_FILE_NAME, StringUtils.defaultIfBlank(originalFileName, DEFAULT_NAME)))
                                .key(key.toString())
                )
                .requestBody(body)
                .build();

        try {
            var result = s3TransferManager.upload(request).completionFuture().get(TIMEOUT_SEC, TimeUnit.SECONDS);
            var checksum = result.response().checksumSHA256();

            log.debug("File is successfully loaded. Checksum received: {}", checksum);
            return checksum;

        } catch (InterruptedException e) {
            log.error(ERROR_UPLOADING_MESSAGE, e);
            Thread.currentThread().interrupt();
            throw new FileUploadException();
        } catch (ExecutionException | TimeoutException e) {
            log.error(ERROR_UPLOADING_MESSAGE, e);
            throw new FileUploadException();
        }
    }

    @Override
    public File downloadFile(@NonNull String bucket, @NonNull String key) {
        log.info("Retrieving file by key '{}' from bucket '{}'", key, bucket);

        try {
            var tempFile = createTempFile(temporaryDirectory);
            s3TransferManager.downloadFile(s -> s.destination(tempFile)
                            .getObjectRequest(request -> request.bucket(bucket).key(key)))
                    .completionFuture().get(TIMEOUT_SEC, TimeUnit.SECONDS);

            return tempFile;
        } catch (InterruptedException e) {
            log.error(ERROR_DOWNLOADING_MESSAGE, e);
            Thread.currentThread().interrupt();
            throw new FileDownloadException();
        } catch (ExecutionException | TimeoutException e) {
            log.error(ERROR_DOWNLOADING_MESSAGE, e);
            throw new FileDownloadException();
        }
    }
}
