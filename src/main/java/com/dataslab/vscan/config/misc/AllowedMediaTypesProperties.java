package com.dataslab.vscan.config.misc;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.application")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Validated
public class AllowedMediaTypesProperties {

    @NotEmpty
    private List<String> allowedMediaTypes;
}
