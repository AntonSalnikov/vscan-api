package com.dataslab.vscan.config.misc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TikaConfiguration {

    @Autowired
    private final FileTypesProperties fileTypesProperties;

    @Bean
    public FileTypeValidator fileTypeValidator() {
        return new FileTypeValidator();
    }


    public class FileTypeValidator {

        private static final MimeTypes MIME_TYPES = TikaConfig.getDefaultConfig().getMimeRepository();
        private final List<String> allowedExtensions;

        FileTypeValidator() {
            this.allowedExtensions = fileTypesProperties.getAllowedFileExtensions();
        }

        public boolean isValid(File file) {

            try {
                var mimeType = MIME_TYPES.getMimeType(file);
                var extensions = mimeType.getExtensions();
                log.debug("Resolved media type '{}' with extensions '{}'", mimeType, extensions);
                return CollectionUtils.containsAny(allowedExtensions, extensions);
            } catch (IOException | MimeTypeException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
