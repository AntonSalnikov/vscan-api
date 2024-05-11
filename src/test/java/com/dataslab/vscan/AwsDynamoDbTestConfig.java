package com.dataslab.vscan;

import com.dataslab.vscan.infra.dynamodb.FileScanResultEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AwsDynamoDbTestConfig {

    private final DynamoDbTable<FileScanResultEntity> dynamoDbTable;

    @PostConstruct
    void createBucket() {

        dynamoDbTable.createTable();
        log.info("Table is created");
    }
}
