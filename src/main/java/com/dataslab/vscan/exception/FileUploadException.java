package com.dataslab.vscan.exception;

public class FileUploadException extends RuntimeException {

    private static final String MESSAGE = "Error appeared while uploading file to the storage";

    public FileUploadException() {
        super(MESSAGE);
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
