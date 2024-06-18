package com.dataslab.vscan.service.domain;

import lombok.NonNull;

public record Verdict(@NonNull String fileVerdict,
                      @NonNull String ESAAVVerdict,
                      @NonNull String ESAAMPVerdict) {
}
