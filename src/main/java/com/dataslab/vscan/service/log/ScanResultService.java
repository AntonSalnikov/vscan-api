package com.dataslab.vscan.service.log;

import com.dataslab.vscan.dto.ValidationStatus;
import com.dataslab.vscan.infra.dynamodb.VerdictEntity;
import com.dataslab.vscan.service.domain.FileVerificationResult;
import com.dataslab.vscan.service.file.FileScanResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScanResultService {

    private final FileScanResultRepository fileScanResultRepository;

    public void process(FileVerificationResult fileVerificationResult)  {
        log.info("Receive result: {}", fileVerificationResult);

        //persist verdict
        fileScanResultRepository.getById(fileVerificationResult.messageId())
                .ifPresentOrElse(file -> {
                    file.setValidationStatus(resolve(fileVerificationResult.verdict().ESAAMPVerdict()));

                    file.setSegHash(fileVerificationResult.hash());
                    file.setVerdict(VerdictEntity.toEntity(fileVerificationResult.verdict()));
                    file.setVerdictReceivedAt(Instant.now());

                    file.setModifiedAt(Instant.now());
                    fileScanResultRepository.save(file);

                    //TODO: trigger webhook
                    },
                () -> log.warn("No file scan result found")); //TODO: consider to throw exception
    }

    private static ValidationStatus resolve(String ESAAMPVerdict) {

        if("FA_PENDING".equals(ESAAMPVerdict)) {
            return ValidationStatus.PROCESSING;
        }

        return ValidationStatus.FINISHED;
    }
}
