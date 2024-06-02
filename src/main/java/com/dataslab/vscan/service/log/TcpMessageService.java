package com.dataslab.vscan.service.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TcpMessageService {

    public void process(byte[] payload)  {
        String message = new String(payload);
        log.info("Receive message: {}", message);
    }
}
