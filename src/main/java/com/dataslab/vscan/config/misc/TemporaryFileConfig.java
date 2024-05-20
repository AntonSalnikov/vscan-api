package com.dataslab.vscan.config.misc;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@Slf4j
public class TemporaryFileConfig {

    private static final Path TEMPORARY_DIRECTORY = createTemporaryDirectory();

    @Bean
    public Path temporaryDirectory() {
        return TEMPORARY_DIRECTORY;
    }

    @PreDestroy
    void delete() {

        try {
            var result = Files.deleteIfExists(TEMPORARY_DIRECTORY);
            log.info("Temporary directory is deleted with result: {}", result);
        } catch (IOException e) {
            log.error("Error appeared while deleting temporary directory", e);
        }
    }

    private static Path createTemporaryDirectory() {
        try {
            return Files.createTempDirectory("tempFiles");
        } catch (IOException e) {
            log.error("Error appeared while creating temp directory");
            throw new IllegalStateException("Temporary directory is not created");
        }
    }
}
