package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.event.binaryContent.BinaryContentUploadFailedEvent;
import com.sprint.mission.discodeit.event.message.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationRequiredTopicListener {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final ChannelService channelService;
    private final SseService sseService;

    @KafkaListener(topics = "discodeit.MessageCreatedEvent")
    public void onMessageCreatedEvent(String kafkaEvent) {
        try {
            MessageCreatedEvent messageCreatedEvent = objectMapper.readValue(kafkaEvent, MessageCreatedEvent.class);
            notificationService.createForMessageCreated(messageCreatedEvent);
        } catch (JsonProcessingException e) {
            log.error("메세지 생성 카프카 수신 오류, kafkaEvent={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "discodeit.RoleUpdatedEvent")
    public void onRoleUpdatedEvent(String kafkaEvent) {
        try {
            RoleUpdatedEvent roleUpdatedEvent = objectMapper.readValue(kafkaEvent, RoleUpdatedEvent.class);
            notificationService.createForRoleUpdate(roleUpdatedEvent);
        } catch (JsonProcessingException e) {
            log.error("role업데이트 카프카 수신 오류, kafkaEvent={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "discodeit.BinaryUploadFailedEvent")
    public void handleFailedUploadBinaryContent(String kafkaEvent) {
        try {
            BinaryContentUploadFailedEvent binaryContentUploadFailedEvent = objectMapper.readValue(kafkaEvent, BinaryContentUploadFailedEvent.class);
            notificationService.createForFailedUpload(binaryContentUploadFailedEvent);
        } catch (JsonProcessingException e) {
            log.error("파일업로드실패 카프카 수신 오류, kafkaEvent={}", kafkaEvent, e);
            throw new RuntimeException(e);
        }
    }

}
