package com.dataslab.vscan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.Collections;

@Configuration
public class AwsSqsConfig {


//    @Bean(destroyMethod = "shutdown")
//    @Primary
//    public AmazonSQSAsync amazonSQSAsync(AwsRegionProperties regionProperties,
//                                         AwsCredentialsProperties awsCredentialsProperties) {
//
//        return AmazonSQSAsyncClient.asyncBuilder()
//                .withCredentials(AwsCredentialsProviderFactory.provider(awsCredentialsProperties.getProfileName()))
//                .withRegion(regionProperties.getStatic())
//                .build();
//    }
//
//    @Bean
//    @Primary
//    public QueueMessagingTemplate SqsQueueSender(AmazonSQSAsync amazonSQSAsync, MessageConverter messageConverter) {
//        QueueMessagingTemplate template = new QueueMessagingTemplate(amazonSQSAsync);
//        template.setMessageConverter(messageConverter);
//
//        return template;
//    }
//
//    @Bean
//    @Primary
//    public MessageConverter messageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//
//        converter.setSerializedPayloadClass(String.class);
//        converter.setObjectMapper(WebMvcConfig.OBJECT_MAPPER);
//
//        return converter;
//    }
//
//    @Bean
//    @Primary
//    public QueueMessageHandlerFactory queueMessageHandlerFactory(MessageConverter messageConverter) {
//        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
//        factory.setMessageConverters(Collections.singletonList(messageConverter));
//        return factory;
//    }
}
