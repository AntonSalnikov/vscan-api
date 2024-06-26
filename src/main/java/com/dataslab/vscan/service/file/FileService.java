package com.dataslab.vscan.service.file;

import com.dataslab.vscan.exception.FileUploadException;
import com.dataslab.vscan.service.domain.FileUploadResult;
import lombok.NonNull;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public interface FileService {


    /**
     * Uploads file to temporal quarantine storage and creates corresponding record in database
     *
     * @param file to be uploaded
     * @return {@link FileUploadResult}
     *
     * @throws FileUploadException if operation fails
     */
    FileUploadResult uploadFile(@NonNull File file, String originalFileName);

    Optional<FileUploadResult> getById(@NonNull UUID id);

    Optional<FileUploadResult> getBySha256Hash(@NonNull String hash);

    void sendToScan(@NonNull String bucket, @NonNull UUID key);
}
