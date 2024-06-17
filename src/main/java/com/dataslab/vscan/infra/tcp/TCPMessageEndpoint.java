package com.dataslab.vscan.infra.tcp;

import com.dataslab.vscan.service.log.TcpMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Map;

import static com.dataslab.vscan.config.misc.TcpIntegrationConfig.TCP_SYSLOG_CHANNEL;

@Slf4j
@MessageEndpoint
@RequiredArgsConstructor
public class TCPMessageEndpoint {

    private final TcpMessageService messageService;


    @ServiceActivator(inputChannel = TCP_SYSLOG_CHANNEL)
    public void process(Map<Object, Object> message) {
        log.debug("Received message: {}", message);
    }

}
