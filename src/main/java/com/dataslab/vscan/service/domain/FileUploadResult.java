package com.dataslab.vscan.service.domain;

import com.dataslab.vscan.dto.ValidationStatus;
import lombok.NonNull;

import java.util.UUID;

public record FileUploadResult(@NonNull UUID id,
                               @NonNull ValidationStatus validationStatus,
                               @NonNull String sha256Hash) {

    public FileUploadResult(@NonNull UUID id, @NonNull String sha256Hash) {
        this(id, ValidationStatus.QUEUED, sha256Hash);
    }
}
