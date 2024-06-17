package com.dataslab.vscan.service.log;

import com.dataslab.vscan.dto.ValidationStatus;
import com.dataslab.vscan.service.domain.ScanResult;
import com.dataslab.vscan.service.file.FileScanResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TcpMessageService {

    private final FileScanResultRepository fileScanResultRepository;

    public void process(ScanResult scanResult)  {
        log.info("Receive result: {}", scanResult);

        //parse log
        var messageId = UUID.fromString(scanResult.verdict());
        var validationStatus = ValidationStatus.FINISHED;

        //persist verdict
        fileScanResultRepository.getById(messageId)
                .ifPresentOrElse(file -> {
                    file.setValidationStatus(validationStatus);

                    file.setSegHash(scanResult.hash());
                    file.setVerdict(scanResult.verdict());
                    file.setVerdictReceivedAt(Instant.now());

                    file.setModifiedAt(Instant.now());
                    fileScanResultRepository.save(file);

                    //TODO: trigger webhook
                    },
                () -> log.warn("No file scan result found")); //TODO: consider to throw exception
    }
}
