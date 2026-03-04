package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

// @Component제거, kafka listener 사용
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {
    private final NotificationService notificationService;
    private final ReadStatusService readStatusService;
    private final UserService userService;

    @Async(value = "eventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageCreatedEvent event) {
        MessageResponseDto responseDto = event.getMessageResponseDto();
        String authorName = responseDto.author().username();
        String channelName = event.getChannelName() == null ? "" : event.getChannelName();

        String title = authorName + " (#" + channelName + ")";
        String content = event.getMessageResponseDto().content();

        List<UUID> receiverIdList = readStatusService.getAllNotificationEnabledByChannelId(responseDto.channelId())
                .stream()
                .map(ReadStatusResponseDto::userId)
                .filter(uuid -> !uuid.equals(responseDto.author().id()))
                .toList();

        notificationService.createMultipleNotification(receiverIdList, title, content);
        log.debug("알림 생성 :{}", receiverIdList);
    }

    @Async(value = "eventExecutor")
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void on(RoleUpdatedEvent event) {
        UUID userId = event.getUserId();
        Role from = event.getFrom();
        Role to = event.getTo();
        String title = "권한이 변경되었습니다.";
        String message = from + " -> " + to;
        notificationService.createNotification(userId, title, message);
        log.debug("{}의 권한 변경 {}", userId, title);
    }

    @Async(value = "eventExecutor")
    @EventListener
    public void on(S3UploadFailedEvent event) {
        String title = "S3 파일 업로드 실패";
        String requestId = event.getRequestId() + "\n";
        String BinaryContentId = event.getBinaryContentId().toString() + "\n";
        String message = event.getSdkClientException().getMessage() + "\n";

        String content = requestId + BinaryContentId + message;

        List<UUID> adminList = userService.getAllUsers().stream()
                .filter(user -> user.role().equals(Role.ADMIN))
                .map(user -> user.id())
                .toList();

        adminList.forEach(adminId -> notificationService.createNotification(adminId, title, content));
    }


}
