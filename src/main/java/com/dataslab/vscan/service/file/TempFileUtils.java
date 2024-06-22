package com.dataslab.vscan.service.file;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
public class TempFileUtils {

    private TempFileUtils() {
    }

    public static File createTempFile(Path temporaryDirectory) {

        try {
            return Files.createTempFile(temporaryDirectory, UUID.randomUUID().toString(), ".tmp").toFile();
        } catch (IOException e) {
            log.error("Error appeared while creating temp file");
            throw new IllegalStateException(e);
        }
    }

    public static File createTempFile(Path temporaryDirectory, @NonNull String suffix) {

        try {
            return Files.createTempFile(temporaryDirectory, UUID.randomUUID().toString(), suffix).toFile();
        } catch (IOException e) {
            log.error("Error appeared while creating temp file");
            throw new IllegalStateException(e);
        }
    }

    public static void deleteFile(File tempFile) {

        try {
            if(tempFile != null && Files.deleteIfExists(tempFile.toPath())) {
                log.debug("File {} is successfully deleted", tempFile);
            }
        } catch (IOException ioException) {
            log.error("Error while deleting file", ioException);
        }
    }
}
