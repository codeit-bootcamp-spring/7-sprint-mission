package com.sprint.mission.discodeit.eventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.dto_Neo.MessageCreatedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.RoleUpdatedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.S3UploadFailedEvent;
import com.sprint.mission.discodeit.service.basic.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationRequiredTopicListener {
    private static final String MESSAGE_CREATED_TOPIC = "discodeit.message-created-event";
    private static final String ROLE_UPDATED_TOPIC = "discodeit.role-updated-event";
    private static final String S3_UPLOAD_FAILED_TOPIC = "discodeit.s3-upload-failed-event";

    private final NotificationsService notificationsService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = MESSAGE_CREATED_TOPIC)
    public void onMessageCreatedEvent(String kafkaEvent) {
        try {
            MessageCreatedEvent event = objectMapper.readValue(kafkaEvent, MessageCreatedEvent.class);
            notificationsService.saveMessageCreatedEvent(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = ROLE_UPDATED_TOPIC)
    public void onRoleUpdatedEvent(String kafkaEvent) {
        try {
            RoleUpdatedEvent event = objectMapper.readValue(kafkaEvent, RoleUpdatedEvent.class);
            notificationsService.saveRoleUpdateEvent(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = S3_UPLOAD_FAILED_TOPIC)
    public void onS3UploadFailedEvent(String kafkaEvent) {
        try {
            S3UploadFailedEvent event = objectMapper.readValue(kafkaEvent, S3UploadFailedEvent.class);
            notificationsService.saveBinaryContentStorageErrorEvent(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
