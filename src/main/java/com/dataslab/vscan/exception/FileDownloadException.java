package com.dataslab.vscan.exception;

public class FileDownloadException extends RuntimeException {

    private static final String MESSAGE = "Error appeared while downloading file to the storage";

    public FileDownloadException() {
        super(MESSAGE);
    }

    public FileDownloadException(String message) {
        super(message);
    }

    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
