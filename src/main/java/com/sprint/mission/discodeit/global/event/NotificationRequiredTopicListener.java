package com.sprint.mission.discodeit.global.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.readstatus.response.NotificationDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationRequiredTopicListener {

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final SseService sseService;

    @KafkaListener(topics = "discodeit.MessageCreatedEvent")
    @Transactional
    public void onMessageCreatedEvent(String kafkaEvent) {
        try {
            MessageCreatedEvent event = objectMapper.readValue(kafkaEvent,
                    MessageCreatedEvent.class);

            UUID channelId = event.channelId();
            UUID messageId = event.messageId();

            Message message = messageRepository.findById(messageId)
                    .orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
            User user = message.getAuthor();
            Channel channel = message.getChannel();

            String title = channel.getChannelType().equals(ChannelType.PRIVATE) ?
                    user.getUsername() + "의 개인 메시지" :
                    user.getUsername() + "(#" + channel.getChannelName() + ")";

            String content = message.getContent().isEmpty() ?
                    "(첨부파일)" :
                    message.getContent();

            // 알림 활성화된 유저 조회
            List<UUID> users = readStatusRepository.findAllByChannelIdAndNotificationEnabled(event.channelId(), true).stream()
                    .map(readStatus -> readStatus.getUser().getId())
                    .filter(userId -> !userId.equals(event.senderId())) // 메시지를 보낸 사람은 제외
                    .toList();

            List<Notification> notifications = new ArrayList<>();

            // 인원 별 채널 알림 생성
            users.forEach(userId -> {
                Notification notification = new Notification(
                        title,
                        content,
                        channelId,
                        userId
                );
                notifications.add(notification);
            });

            // SSE 이벤트 전송
            List<Notification> saved = notificationRepository.saveAll(notifications);

            List<NotificationDto> dtos = saved.stream()
                    .map(notificationMapper::toNotificationDto)
                    .toList();

            dtos.forEach(dto -> {
                sseService.send(
                        List.of(dto.receiverId()),
                        "notifications.created",
                        dto
                );
            });

            log.info("Notification created for MessageCreatedEvent: {}", messageId);
        } catch (Exception e) {
            log.error("Failed to process MessageCreatedEvent", e);
        }
    }

    @KafkaListener(topics = "discodeit.RoleUpdatedEvent")
    @Transactional
    public void onRoleUpdatedEvent(String kafkaEvent) {
        try {
            RoleUpdatedEvent event = objectMapper.readValue(kafkaEvent, RoleUpdatedEvent.class);

            String title = "권한이 변경되었습니다.";
            String content = event.oldRole() + "->" + event.newRole();

            Notification notification = new Notification(
                    title,
                    content,
                    null,
                    event.userId()
            );

            notificationRepository.save(notification);

            sseService.send(
                    List.of(event.userId()),
                    "notification.created",
                    notificationMapper.toNotificationDto(notification)
            );

            log.info("Notification created for RoleUpdatedEvent: {}", event.userId());
        } catch (Exception e) {
            log.error("Failed to process RoleUpdatedEvent", e);
        }
    }

    @KafkaListener(topics = "discodeit.S3UploadFailEvent")
    public void onS3UploadFailedEvent(String kafkaEvent) {
        try {
            S3UploadFailEvent event = objectMapper.readValue(kafkaEvent, S3UploadFailEvent.class);

            String requestId = MDC.get("requestId");

            String errorMessage = String.format("""
                [S3 Upload Fail]
                
                Task: S3 Binary Upload
                RequestId: %s
                BinaryContentId: %s
                Error: %s
                """,
                    requestId,
                    event.binaryContentId(),
                    event.errorMessage()
            );

            List<User> admins = userRepository.findAllByRole(Role.ADMIN);
            List<Notification> notifications = new ArrayList<>();

            admins.forEach(user -> {
                Notification notification = new Notification(
                        "S3 파일 업로드 실패",
                        errorMessage,
                        null,
                        user.getId()
                );

                notifications.add(notification);
            });

            // sse 이벤트 추가 필요

            notificationRepository.saveAll(notifications);
            log.error("S3 업로드 최종 실패 - binaryContentId: {}", event.binaryContentId());

        } catch (Exception e) {
            log.error("Failed to process S3UploadFailEvent", e);
        }
    }
}
