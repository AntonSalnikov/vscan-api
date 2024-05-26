package com.dataslab.vscan.infra.sqs;

import com.dataslab.vscan.service.file.FileService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@AllArgsConstructor
@Slf4j
public class FileUploadListener {

    private final FileService fileService;

    @SqsListener(queueNames = "${spring.cloud.aws.sqs.queue.file-upload-queue}", factory = "defaultSqsListenerContainerFactory")
    public void listen(@Payload S3EventNotification event) {
        log.info("Consuming event {}", event);

        Optional.ofNullable(event)
                .map(S3EventNotification::records)
                .orElseGet(List::of)
                .stream()
                .map(S3EventNotificationRecord::s3)
                .forEach(s3 -> fileService.sendToScan(resolveBucket(s3), resolveKey(s3)));
    }

    private UUID resolveKey(S3 s3) {
        return Optional.ofNullable(s3)
                .map(S3::object)
                .map(S3Object::key)
                .map(UUID::fromString)
                .orElseThrow(() -> new IllegalArgumentException("Object key is not resolved"));
    }

    private String resolveBucket(S3 s3) {
        return Optional.ofNullable(s3)
                .map(S3::bucket)
                .map(S3Bucket::name)
                .orElseThrow(() -> new IllegalArgumentException("Bucket is not resolved"));
    }
}
