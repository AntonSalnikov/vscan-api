package com.dataslab.vscan.infra.tcp;

import com.dataslab.vscan.infra.sqs.SegLogPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Map;
import java.util.Optional;

import static com.dataslab.vscan.config.misc.TcpIntegrationConfig.TCP_SYSLOG_CHANNEL;

@Slf4j
@MessageEndpoint
@RequiredArgsConstructor
public class TCPMessageEndpoint {

    private static final String SYSLOG_UNDECODED_KEY = "syslog_UNDECODED";
    private final SegLogPublisher segLogPublisher;


    @ServiceActivator(inputChannel = TCP_SYSLOG_CHANNEL)
    public void process(Map<String, String> message) {
        log.debug("Received message: {}", message);

        var payload = Optional.ofNullable(message.get(SYSLOG_UNDECODED_KEY))
                .orElseThrow(() -> new IllegalStateException("No syslog payload is found."));
        segLogPublisher.publish(payload);
    }

}
