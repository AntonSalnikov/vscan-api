package com.dataslab.vscan.dto;

import java.util.UUID;

public record FileUploadResultDTO(UUID id,
                                  ValidationStatus validationStatus,
                                  String sha256Hash,
                                  ScanResult scanResult) {

    public FileUploadResultDTO(UUID id, ValidationStatus validationStatus, String sha256Hash) {
        this(id, validationStatus, sha256Hash, ScanResult.UNKNOWN);
    }
}
