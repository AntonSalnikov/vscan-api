package com.dataslab.vscan.infra.sqs;

public record S3Object(String key,
                       Long size,
                       String eTag,
                       String versionId,
                       String sequencer) {
}
