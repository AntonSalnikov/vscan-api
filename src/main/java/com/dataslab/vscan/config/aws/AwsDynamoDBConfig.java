package com.dataslab.vscan.config.aws;

import com.dataslab.vscan.infra.dynamodb.FileScanResultEntity;
import io.awspring.cloud.autoconfigure.dynamodb.DynamoDbProperties;
import jdk.jfr.Name;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class AwsDynamoDBConfig {

    @Bean
    @Primary
    public DynamoDbClient dynamoDbClient(DynamoDbProperties dynamoDbProperties,
                                         AwsCredentialsProvider provider) {

        return DynamoDbClient.builder()
                .credentialsProvider(provider)
                .region(AwsUtil.region(dynamoDbProperties.getRegion()))
                .endpointOverride(dynamoDbProperties.getEndpoint()) //Used for tests. If null fall back to default.
                .build();
    }

    @Bean
    @Name("fileScanResultTable")
    public DynamoDbTable<FileScanResultEntity> fileScanResultTable(DynamoDbClient dynamoDbClient,
                                                                   CustomDynamoDbProperties properties) {
        log.info("Creating DynamoDB Table Bean with {}", properties);
        DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        TableSchema<FileScanResultEntity> ticketDocumentSchema = TableSchema.fromBean(FileScanResultEntity.class);
        return dynamoDbEnhancedClient.table(properties.getFileScanResultTable(), ticketDocumentSchema);
    }
}
