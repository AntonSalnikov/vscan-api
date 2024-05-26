package com.dataslab.vscan.infra.sqs;

import java.time.Instant;

public record S3EventNotificationRecord(String awsRegion,
                                        String eventName,
                                        String eventSource,
                                        String eventVersion,
                                        S3 s3,
                                        Instant eventTime) {
}
