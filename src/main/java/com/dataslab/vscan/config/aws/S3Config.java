package com.dataslab.vscan.config.aws;


import io.awspring.cloud.autoconfigure.s3.properties.S3Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

@Configuration
@Slf4j
public class S3Config {


    @Bean
    @Primary
    public S3AsyncClient s3AsyncClient(S3Properties s3Properties,
                                       AwsCredentialsProvider provider) {

        var region = AwsUtil.region(s3Properties.getRegion());

        return  S3AsyncClient.builder()
                .credentialsProvider(provider)
                .region(region)
                .endpointOverride(s3Properties.getEndpoint()) //Used for tests. If null fall back to default.
                .build();
    }

    @Bean
    public S3TransferManager transferManager(S3AsyncClient s3AsyncClient) {

        return  S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .build();
    }
}
