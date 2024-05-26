package com.dataslab.vscan.infra.sqs;

import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.eventnotifications.s3.model.S3EventNotificationRecord;

import java.util.List;

@Data
@NoArgsConstructor
public class S3EventNotification {

    private final List<S3EventNotificationRecord> records = List.of();
}
