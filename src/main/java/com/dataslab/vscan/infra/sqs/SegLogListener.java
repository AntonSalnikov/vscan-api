package com.dataslab.vscan.infra.sqs;

import com.dataslab.vscan.infra.tcp.SegLogParser;
import com.dataslab.vscan.service.log.ScanResultService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SegLogListener {

    private final ScanResultService messageService;

    @SqsListener(queueNames = "${spring.cloud.aws.sqs.queue.seg-log-queue}", factory = "defaultSqsListenerContainerFactory")
    public void listen(@Payload String segLogQueue) {
        log.info("Consuming event with SEG log {}", segLogQueue);
        messageService.process(SegLogParser.parse(segLogQueue));
    }
}
