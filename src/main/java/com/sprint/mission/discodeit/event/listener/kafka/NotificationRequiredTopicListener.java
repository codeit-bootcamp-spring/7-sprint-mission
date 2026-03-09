package com.sprint.mission.discodeit.event.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.event.SseEvent;
import com.sprint.mission.discodeit.global.sse.SseService;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationRequiredTopicListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final ReadStatusService readStatusService;
    private final UserService userService;
    private final SseService sseService;

    @KafkaListener(topics = "discodeit.MessageCreatedEvent")
    public void onMessageCreatedEvent(String kafkaEvent) {
        try {
            MessageCreatedEvent event = objectMapper.readValue(kafkaEvent, MessageCreatedEvent.class);

            MessageResponseDto responseDto = event.getMessageResponseDto();
            String authorName = responseDto.author().username();
            String channelName = event.getChannelName() == null ? "" : event.getChannelName();

            String title = authorName + " (#" + channelName + ")";
            String content = responseDto.content();

            List<UUID> receiverIdList = readStatusService.getAllNotificationEnabledByChannelId(responseDto.channelId())
                    .stream()
                    .map(ReadStatusResponseDto::userId)
                    .filter(uuid -> !uuid.equals(responseDto.author().id()))
                    .toList();

            notificationService.createMultipleNotification(receiverIdList, title, content);
            log.debug("알림 생성: {}", receiverIdList);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "discodeit.RoleUpdatedEvent")
    public void onRoleUpdatedEvent(String kafkaEvent) {
        try {
            RoleUpdatedEvent event = objectMapper.readValue(kafkaEvent, RoleUpdatedEvent.class);

            UUID userId = event.getUserId();
            Role from = event.getFrom();
            Role to = event.getTo();
            String title = "권한이 변경되었습니다.";
            String message = from + " -> " + to;

            notificationService.createNotification(userId, title, message);
            log.debug("{}의 권한 변경: {}", userId, title);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "discodeit.S3UploadFailedEvent")
    public void onS3UploadFailedEvent(String kafkaEvent) {
        try {
            S3UploadFailedEvent event = objectMapper.readValue(kafkaEvent, S3UploadFailedEvent.class);

            String title = "S3 파일 업로드 실패";
            String requestId = event.getRequestId() + "\n";
            String binaryContentId = event.getBinaryContentId().toString() + "\n";
            String message = event.getSdkClientException().getMessage() + "\n";
            String content = requestId + binaryContentId + message;

            List<UUID> adminList = userService.getAllUsers().stream()
                    .filter(user -> user.role().equals(Role.ADMIN))
                    .map(user -> user.id())
                    .toList();

            adminList.forEach(adminId -> notificationService.createNotification(adminId, title, content));
            log.debug("S3 업로드 실패 알림 발송 완료");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "discodeit.SseEvent")
    public void onSseEvent(String kafkaEvent) {
        try {
            SseEvent event = objectMapper.readValue(kafkaEvent, SseEvent.class);
            log.debug("SSE 이벤트 실행: {}", event.eventName());
            if (event.isBroadCast()) {
                sseService.broadcast(event.eventName(), event.data());
            } else {
                sseService.send(event.receiverId(), event.eventName(), event.data());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
