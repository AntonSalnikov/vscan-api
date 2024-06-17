package com.dataslab.vscan.infra.dynamodb;

import com.dataslab.vscan.dto.ValidationStatus;
import com.dataslab.vscan.service.domain.FileUploadResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
@Data
@NoArgsConstructor
public class FileScanResultEntity {

    public static final String SHA_256_HASH_INDEX_NAME = "sha256Hash-index";

    private UUID id;
    private String originalFileName;
    private long fileSize;
    private String sha256Hash;
    private ValidationStatus validationStatus = ValidationStatus.QUEUED;

    private String verdict;
    private String segHash;
    private Instant verdictReceivedAt;

    private Instant createdAt = Instant.now();
    private Instant modifiedAt;

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = SHA_256_HASH_INDEX_NAME)
    public String getSha256Hash() {
        return sha256Hash;
    }

    public static FileScanResultEntity createNew(@NonNull UUID id,
                                                 @NonNull String originalFileName,
                                                 long fileSize,
                                                 @NonNull String sha256Hash) {
        var entity = new FileScanResultEntity();
        entity.setId(id);
        entity.setOriginalFileName(originalFileName);
        entity.setFileSize(fileSize);
        entity.setSha256Hash(sha256Hash);

        return entity;
    }

    public FileUploadResult to() {
        return new FileUploadResult(id, validationStatus, sha256Hash);
    }
}
