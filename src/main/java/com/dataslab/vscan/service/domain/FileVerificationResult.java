package com.dataslab.vscan.service.domain;

import lombok.NonNull;

import java.util.UUID;

public record FileVerificationResult(String hash,
                                     @NonNull Verdict verdict,
                                     @NonNull UUID messageId) {
}
