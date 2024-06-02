package com.dataslab.vscan.infra.tcp;

import com.dataslab.vscan.service.log.TcpMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.dataslab.vscan.config.misc.TcpIntegrationConfig.TCP_DEFAULT_CHANNEL;

@Slf4j
@MessageEndpoint
@RequiredArgsConstructor
public class TCPMessageEndpoint {

    private static final byte[] RESPONSE = "ACCEPTED".getBytes();
    private final TcpMessageService messageService;

    @ServiceActivator(inputChannel = TCP_DEFAULT_CHANNEL)
    public byte[] process(byte[] message) {
        messageService.process(message);
        return RESPONSE;
    }

}
