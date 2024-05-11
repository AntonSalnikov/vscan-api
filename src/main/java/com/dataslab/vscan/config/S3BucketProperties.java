package com.dataslab.vscan.config;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
@ToString@EqualsAndHashCode
@NoArgsConstructor
@Getter@Setter
@Validated
public class S3BucketProperties {

    @NotBlank
    private String dirtyBucket;
}
