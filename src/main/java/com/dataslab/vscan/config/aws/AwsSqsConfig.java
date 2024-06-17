package com.dataslab.vscan.config.aws;

import com.dataslab.vscan.config.web.WebMvcConfig;
import io.awspring.cloud.autoconfigure.sqs.SqsProperties;
import io.awspring.cloud.sqs.config.SqsListenerConfigurer;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@Slf4j
@ConditionalOnProperty("spring.cloud.aws.sqs.enabled")
public class AwsSqsConfig {


    @Primary
    @Bean
    public SqsAsyncClient amazonSQSAsync(AwsCredentialsProvider provider,
                                         SqsProperties sqsProperties) {
        log.info("Creating SqsAsyncClient bean with {}", sqsProperties);
        var region = AwsUtil.region(sqsProperties.getRegion());

        return SqsAsyncClient.builder()
                .credentialsProvider(provider)
                .region(region)
                .endpointOverride(sqsProperties.getEndpoint()) //Used for tests. If null fall back to default.
                .build();
    }

    @Bean
    @Primary
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @Bean
    SqsListenerConfigurer configurer() {
        return registrar -> registrar.setObjectMapper(WebMvcConfig.OBJECT_MAPPER);
    }

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }
}
