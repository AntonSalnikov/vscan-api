package com.dataslab.vscan.infra.sqs;

public record S3(S3Bucket bucket,
                 S3Object object) {
}
