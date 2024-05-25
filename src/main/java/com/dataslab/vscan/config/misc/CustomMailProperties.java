package com.dataslab.vscan.config.misc;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CustomMailProperties {

    @NotBlank
    private String to;

    @NotBlank
    private String from;

    @NotBlank
    private String subject;
}
