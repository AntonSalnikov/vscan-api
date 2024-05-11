package com.dataslab.vscan.web;

import com.dataslab.vscan.dto.FileUploadResultDTO;
import com.dataslab.vscan.service.domain.FileUploadResult;
import com.dataslab.vscan.service.file.FileService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
@Slf4j
@Validated
public class FileUploadController {

    private static final String TEMP_FILE_SUFFIX = ".tmp";
    private final Path temporaryDirectory;
    private final FileService fileService;

    @PostMapping("/upload")
    public FileUploadResultDTO upload(@NotNull @RequestParam MultipartFile file) {

        var fileId = UUID.randomUUID();
        var originalFileName = Optional.ofNullable(file.getOriginalFilename())
                .filter(StringUtils::isNotBlank)
                .orElseGet(fileId::toString);

        try {
            var tempFile = Files.createTempFile(temporaryDirectory, fileId.toString(), TEMP_FILE_SUFFIX).toFile();
            file.transferTo(tempFile);

            return convert(fileService.uploadFile(tempFile));

        } catch (IOException e) {
            log.error("Error appeared while loading file with originalFileName {}", originalFileName, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error appeared while loading file");
        }
    }

    private static FileUploadResultDTO convert(FileUploadResult result) {

        return new FileUploadResultDTO(result.id(), result.validationStatus(), result.sha256Hash());
    }
}
