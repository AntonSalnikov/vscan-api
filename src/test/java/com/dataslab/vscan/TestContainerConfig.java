package com.dataslab.vscan;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Slf4j
public class TestContainerConfig {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack:3.4.0");

        private static final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);

        private static final LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_IMAGE_NAME)
                .withServices(DYNAMODB, S3)
                .withLogConsumer(logConsumer);

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            log.debug("Starting test containers");

            Startables.deepStart(Stream.of(localStackContainer))
                    .join();

            log.info("LocalStack started: {}", localStackContainer.getContainerInfo());

            System.setProperty("aws.accessKeyId", localStackContainer.getAccessKey());
            System.setProperty("aws.secretAccessKey", localStackContainer.getSecretKey());
            TestPropertyValues.of(
                    "spring.cloud.aws.s3.region:" + localStackContainer.getRegion(),
                    "spring.cloud.aws.dynamodb.region:" + localStackContainer.getRegion(),
                    "spring.cloud.aws.s3.endpoint:" + localStackContainer.getEndpoint(),
                    "spring.cloud.aws.dynamodb.endpoint:" + localStackContainer.getEndpoint()
            ).applyTo(applicationContext);
        }
    }
}
