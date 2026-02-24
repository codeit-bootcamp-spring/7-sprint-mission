package com.sprint.mission.discodeit.global.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProduceRequiredEventListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "discodeit.MessageCreatedEvent",
                    event.channelId().toString(),
                    payload);

            log.info("Kafka publish - MessageCreatedEvent: {}", event.messageId());
        } catch (JsonProcessingException e) {
            log.error("Kafka publish failed", e);
        }
    }

    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    public void on(RoleUpdatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "discodeit.RoleUpdatedEvent",
                    event.userId().toString(),
                    payload
            );

            log.info("Kafka publish - RoleUpdatedEvent: {}", event.userId());

        } catch (Exception e) {
            log.error("Kafka publish failed", e);
        }
    }

    @Async("asyncTaskExecutor")
    @EventListener
    public void on(S3UploadFailEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "discodeit.S3UploadFailEvent",
                    event.binaryContentId().toString(),
                    payload
            );

            log.info("Kafka publish - S3UploadFailEvent: {}", event.binaryContentId());

        } catch (Exception e) {
            log.error("Kafka publish failed", e);
        }
    }
}
