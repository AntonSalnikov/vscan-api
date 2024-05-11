package com.dataslab.vscan.service.file;

import com.dataslab.vscan.infra.dynamodb.FileScanResultEntity;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface FileScanResultRepository {

    FileScanResultEntity save(@NonNull FileScanResultEntity entity);

    Optional<FileScanResultEntity> getById(@NonNull UUID id);

    Optional<FileScanResultEntity> getByHash(@NonNull String sha256Hash);
}
