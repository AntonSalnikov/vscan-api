package com.dataslab.vscan.service.domain;

import lombok.NonNull;

import java.util.UUID;

public record ScanResult(String hash,
                         @NonNull String verdict,
                         @NonNull UUID messageId) {
}
