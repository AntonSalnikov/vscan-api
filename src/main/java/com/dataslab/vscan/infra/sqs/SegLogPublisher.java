package com.dataslab.vscan.infra.sqs;


import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SegLogPublisher {


    private final SqsTemplate template;
    private final String segLogQueue;

    @Autowired
    public SegLogPublisher(SqsTemplate template,
                           @Value("${spring.cloud.aws.sqs.queue.seg-log-queue}") String segLogQueue) {
        this.template = template;
        this.segLogQueue = segLogQueue;
    }

    public void publish(@NonNull String segLog) {
        log.debug("Publishing log to queue {}", segLogQueue);
        var result = template.send(segLog, segLog);

        log.debug("Seg log successfully published with id: {}", result.messageId());
    }

}
