package com.dataslab.vscan.service.log;

import com.dataslab.vscan.dto.ValidationStatus;
import com.dataslab.vscan.service.file.FileScanResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class TcpMessageService {

    private static final String LOG_PATTERN = "";
    private FileScanResultRepository fileScanResultRepository;

    public void process(byte[] payload)  {
        String message = new String(payload);
        log.info("Receive message: {}", message);

        //parse log
        var messageId = UUID.randomUUID();
        var validationStatus = ValidationStatus.VALID;

        //persist verdict
        fileScanResultRepository.getById(messageId)
                .ifPresentOrElse(file -> {
                    file.setValidationStatus(validationStatus);
                    file.setModifiedAt(Instant.now());
                    fileScanResultRepository.save(file);

                    //TODO: trigger webhook
                    },
                () -> log.warn("No file scan result found")); //TODO: consider to throw exception
    }
}
