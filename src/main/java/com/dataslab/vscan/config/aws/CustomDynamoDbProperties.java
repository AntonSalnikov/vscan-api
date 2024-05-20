package com.dataslab.vscan.config.aws;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws.dynamodb")
@ToString@EqualsAndHashCode
@NoArgsConstructor
@Getter@Setter
@Validated
public class CustomDynamoDbProperties {

    @NotBlank
    private String fileScanResultTable;
}
