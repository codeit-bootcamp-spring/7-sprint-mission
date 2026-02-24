package com.sprint.mission.discodeit.global.event;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// @Component // 카프카 발행시 리스너 제외
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {

    private final NotificationRepository notificationRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(MessageCreatedEvent event) {
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

        // 채널 내 알림을 활성화한 인원 조회
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

        notificationRepository.saveAll(notifications);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(RoleUpdatedEvent event) {
        String title = "권한이 변경되었습니다.";
        String content = event.oldRole() + "->" + event.newRole();

        Notification notification = new Notification(
                title,
                content,
                null,
                event.userId()
        );

        notificationRepository.save(notification);
    }

    @EventListener
    @Transactional
    public void on(S3UploadFailEvent event) {

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

        notificationRepository.saveAll(notifications);
        log.error("S3 업로드 최종 실패 - binaryContentId: {}", event.binaryContentId());
    }
}
