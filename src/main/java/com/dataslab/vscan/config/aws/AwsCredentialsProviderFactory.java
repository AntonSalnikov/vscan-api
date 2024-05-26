package com.dataslab.vscan.config.aws;

import io.awspring.cloud.autoconfigure.core.CredentialsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.*;


@Slf4j
@Configuration
@EnableConfigurationProperties(CredentialsProperties.class)
public class AwsCredentialsProviderFactory {

    @Bean
    @Primary
    public AwsCredentialsProvider provider(CredentialsProperties credentialsProperties) {
        return AwsCredentialsProviderChain.of(
                EnvironmentVariableCredentialsProvider.create(),
                InstanceProfileCredentialsProvider.create(),
                ProfileCredentialsProvider.create(AwsUtil.profile(credentialsProperties.getProfile())),
                SystemPropertyCredentialsProvider.create(),
                ContainerCredentialsProvider.builder().build()
        );
    }
}
