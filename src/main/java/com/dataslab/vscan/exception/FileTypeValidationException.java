package com.dataslab.vscan.exception;

public class FileTypeValidationException extends RuntimeException {

    private static final String MESSAGE = "Error appeared while validating file type";

    public FileTypeValidationException() {
        super(MESSAGE);
    }

    public FileTypeValidationException(String message) {
        super(message);
    }
}
