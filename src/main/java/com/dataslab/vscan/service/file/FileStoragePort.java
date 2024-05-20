package com.dataslab.vscan.service.file;

import com.dataslab.vscan.exception.FileUploadException;
import lombok.NonNull;

import java.io.File;
import java.util.UUID;

public interface FileStoragePort {

    /**
     * Uploads file to file storage with specified key
     *
     * @param key unique key used to identify file
     * @param file file to be uploaded
     * @param originalFileName original file name provided by the client.
     *
     * @return SHA256 checksum of the uploaded file
     * @throws FileUploadException if upload fails
     */
    String uploadFile(@NonNull UUID key, @NonNull File file, String originalFileName);
}
