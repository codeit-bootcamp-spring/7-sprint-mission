package com.sprint.mission.discodeit.event.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.event.SseEvent;
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

    @Async("eventExecutor")
    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("discodeit.MessageCreatedEvent", payload);
            log.debug("Kafka MessageCreatedEvent: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("MessageCreatedEvent 직렬화 실패", e);
        }
    }

    @Async("eventExecutor")
    @TransactionalEventListener
    public void on(RoleUpdatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("discodeit.RoleUpdatedEvent", payload);
            log.debug("Kafka MessageCreatedEvent: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("MessageCreatedEvent 직렬화 실패", e);
        }
    }

    @Async("eventExecutor")
    @EventListener
    public void on(S3UploadFailedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("discodeit.S3UploadFailedEvent", payload);
            log.debug("Kafka S3UploadFailedEvent 발행: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("S3UploadFailedEvent 직렬화 실패", e);
        }
    }

    @Async("eventExecutor")
    @TransactionalEventListener
    public void on(SseEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("discodeit.SseEvent", payload);
            log.debug("Kafka SSeEvent 발행: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("SseEvent 직렬화 실패", e);
        }

    }
}