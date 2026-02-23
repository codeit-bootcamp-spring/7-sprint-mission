package com.sprint.mission.discodeit.event.Listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProduceRequiredEventListener {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener
    @Async("notificationExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(MessageCreatedEvent event) throws JsonProcessingException {
        String payload  = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("discodeit.MessageCreatedEvent", payload);

    }

    @TransactionalEventListener
    @Async("notificationExecutor")
    public void on(RoleUpdatedEvent event) throws JsonProcessingException {
        String payload  = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("discodeit.NotificationCreatedEvent", payload);
    }
    @TransactionalEventListener
    @Async("notificationExecutor")
    public void on(S3UploadFailedEvent event) throws JsonProcessingException {

        String payload  = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("discodeit.S3uploadFailedEvent", payload);

    }
}
