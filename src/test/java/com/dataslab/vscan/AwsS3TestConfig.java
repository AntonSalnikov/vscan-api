package com.dataslab.vscan;

import com.dataslab.vscan.config.aws.S3BucketProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.Event;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AwsS3TestConfig {

    private final S3AsyncClient s3AsyncClient;
    private final S3BucketProperties bucketProperties;

    @PostConstruct
    void createBucket() {
        var request = CreateBucketRequest.builder()
                .bucket(bucketProperties.getDirtyBucket())
                .build();

        var response = s3AsyncClient.createBucket(request).join();

        s3AsyncClient.putBucketNotificationConfiguration(
                req -> req.bucket(bucketProperties.getDirtyBucket())
                        .notificationConfiguration(
                                config -> config.queueConfigurations(
                                        qc -> qc.events(Event.S3_OBJECT_CREATED).queueArn("arn:aws:sqs:eu-central-1:000000000000:FileData")
                                )
                        )
        );

        log.info("Bucket is created: {}", response);
    }
}
