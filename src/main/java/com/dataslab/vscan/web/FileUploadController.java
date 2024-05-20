package com.dataslab.vscan.web;

import com.dataslab.vscan.dto.FileUploadResultDTO;
import com.dataslab.vscan.service.domain.FileUploadResult;
import com.dataslab.vscan.service.file.FileService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FileUploadResultDTO upload(@NotNull @RequestParam(name = "file") MultipartFile file) {
        log.debug("Uploading file {}", file);

        if(file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file is not present");
        }

        var fileId = UUID.randomUUID();
        var originalFileName = Optional.ofNullable(file.getOriginalFilename())
                .map(String::trim)
                .map(s -> StringUtils.substring(s, 0, 100))
                .filter(StringUtils::isNotBlank)
                .orElseGet(fileId::toString);

        try {
            var tempFile = Files.createTempFile(temporaryDirectory, fileId.toString(), TEMP_FILE_SUFFIX).toFile();
            file.transferTo(tempFile);

            return convert(fileService.uploadFile(tempFile, originalFileName));

        } catch (IOException e) {
            log.error("Error appeared while loading file with originalFileName {}", originalFileName, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error appeared while loading file");
        }
    }


    @GetMapping(value = "/{fileUploadResultId}")
    public FileUploadResultDTO getFileUploadResultById(@PathVariable UUID fileUploadResultId) {
        log.debug("Retrieving file upload result by id by {}", fileUploadResultId);

        return fileService.getById(fileUploadResultId)
                .map(FileUploadController::convert)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No file upload result found with specified id"));
    }



    private static FileUploadResultDTO convert(FileUploadResult result) {

        return new FileUploadResultDTO(result.id(), result.validationStatus(), result.sha256Hash());
    }
}
