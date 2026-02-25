package com.sprint.mission.discodeit.event.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationRequiredTopicListener {

    private final ObjectMapper objectMapper;
    private final ReadStatusRepository readStatusRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    /* 알림 확인
    /opt/kafka/bin $ ./kafka-console-consumer.sh --topic discodeit.MessageCreatedEvent --from-beginning --bootstrap-server broker:29092

    {"channelId":"26f740d8-7be5-4e9d-831d-2d795e597e7f","channelName":null,"authorName":"test","content":"gd"}
    {"channelId":"26f740d8-7be5-4e9d-831d-2d795e597e7f","channelName":null,"authorName":"admin","content":"ㅎㅇ"}
    ^CProcessed a total of 2 messages
     */
    @KafkaListener(topics = "discodeit.MessageCreatedEvent")
    @Transactional
    public void onMessageCreatedEvent(String kafkaEvent) {
        try {
            MessageCreatedEvent event = objectMapper.readValue(kafkaEvent,
                    MessageCreatedEvent.class);

            List<ReadStatus> subscriptions
                    = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(
                    event.channelId());

            List<Notification> notifications = subscriptions.stream()
                    .map(ReadStatus::getUser)
                    .filter(user -> !user.getUsername().equals(event.authorName()))
                    .map(user -> Notification.builder()
                            .user(user)
                            .title(String.format("%s (#%s)", event.authorName(), event.channelName()))
                            .content(event.content())
                            .build())
                    .toList();
            notificationRepository.saveAll(notifications);

            if (cacheManager.getCache("notification") != null) {
                notifications.forEach(n -> {
                    Objects.requireNonNull(cacheManager.getCache("notification")).evict(n.getUser().getId());
                    log.debug("메시지 알림 캐시 무효화: userId={}", n.getUser().getId());
                });
            }

        } catch (JsonProcessingException e) {
            log.error("Kafka message parsing error: ", e);
            throw new RuntimeException(e);
        }
    }

    /* 알림 확인
    /opt/kafka/bin $ ./kafka-console-consumer.sh --topic discodeit.RoleUpdatedEvent --from-beginning --bootstrap-server broker:29092
    {"userId":"1fd1564a-7097-4584-bc43-9114cee6c5ed","oldRole":"USER","newRole":"ADMIN"}
    {"userId":"1fd1564a-7097-4584-bc43-9114cee6c5ed","oldRole":"ADMIN","newRole":"USER"}
    ^CProcessed a total of 2 messages
     */
    @KafkaListener(topics = "discodeit.RoleUpdatedEvent")
    @Transactional
    public void onRoleUpdatedEvent(String kafkaEvent) {
        try {
            RoleUpdatedEvent event = objectMapper.readValue(kafkaEvent,
                    RoleUpdatedEvent.class);

            User user = userRepository.findById(event.userId())
                    .orElseThrow(() -> new UserNotFoundException(event.userId()));

            Notification notification = Notification.builder()
                    .user(user)
                    .title("권한이 변경되었습니다.")
                    .content(String.format("%s -> %s", event.oldRole(), event.newRole()))
                    .build();
            notificationRepository.save(notification);

            if (cacheManager.getCache("notification") != null) {
                Objects.requireNonNull(cacheManager.getCache("notification")).evict(user.getId());
                log.info("권한 알림 캐시 무효화: userId={}", user.getId());
            }

        } catch (JsonProcessingException e) {
            log.error("Kafka message parsing error: ", e);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "discodeit.S3UploadFailedEvent")
    @Transactional
    public void onS3UploadFailedEvent(String kafkaEvent) {
        try {
            S3UploadFailedEvent event = objectMapper.readValue(kafkaEvent,
                    S3UploadFailedEvent.class);

            log.debug("Kafka: S3 업로드 실패 알림 - id={}", event.binaryContentId());


        } catch (JsonProcessingException e) {
            log.error("Kafka message parsing error: ", e);
            throw new RuntimeException(e);
        }
    }
}
