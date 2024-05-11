package com.dataslab.vscan.infra.dynamodb;

import com.dataslab.vscan.service.file.FileScanResultRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.dataslab.vscan.infra.dynamodb.FileScanResultEntity.SHA_256_HASH_INDEX_NAME;

@Component
@AllArgsConstructor
@Slf4j
public class ScanResultDynamoDbAdapter implements FileScanResultRepository {

    private final DynamoDbTable<FileScanResultEntity> dynamoDbTable;

    @Override
    public FileScanResultEntity save(@NonNull FileScanResultEntity entity) {
        log.debug("Saving {}", entity);
        return dynamoDbTable.updateItem(entity);
    }


    @Override
    public Optional<FileScanResultEntity> getById(@NonNull UUID id) {
        log.debug("Getting entity by id {}", id);

        var key = createSKey(createSAttributeValue(id.toString()));
        return Optional.ofNullable(dynamoDbTable.getItem(key));
    }

    @Override
    public Optional<FileScanResultEntity> getByHash(@NonNull String sha256Hash) {
        log.debug("Getting item by sha256 hash '{}'", sha256Hash);

        var attributeValue = createSAttributeValue(sha256Hash);
        var queryConditional = QueryConditional.keyEqualTo(createSKey(attributeValue));

        return dynamoDbTable.index(SHA_256_HASH_INDEX_NAME).query(queryConditional).stream()
                .findFirst()
                .map(Page::items)
                .map(List::stream)
                .flatMap(Stream::findFirst);
    }

    private static AttributeValue createSAttributeValue(String value) {
        return AttributeValue.builder()
                .s(value)
                .build();
    }

    private static Key createSKey(AttributeValue attributeValue) {
        return Key.builder()
                .partitionValue(attributeValue)
                .build();
    }
}
