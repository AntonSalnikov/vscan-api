package com.dataslab.vscan.infra.sqs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record S3EventNotification(@JsonProperty("Records") List<S3EventNotificationRecord> records) {
}
