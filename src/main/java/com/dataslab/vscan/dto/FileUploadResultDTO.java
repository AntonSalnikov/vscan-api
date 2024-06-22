package com.dataslab.vscan.dto;

import java.util.UUID;

public record FileUploadResultDTO(UUID id,
                                  ValidationStatus validationStatus,
                                  String sha256Hash,
                                  ScanResult scanResult) {
}
