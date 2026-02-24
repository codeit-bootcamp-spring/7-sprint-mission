package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {
    private final NotificationService notificationService;
    private final ReadStatusService readStatusService;

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
        log.info("알림 생성 :{}", receiverIdList);
    }

    @Async(value = "eventExecutor")
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void on(RoleUpdatedEvent event) {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        UUID userId = event.getUserId();
        Role from = event.getFrom();
        Role to = event.getTo();
        String title = auth.getName() + "(으)로부터 권한이 변경되었습니다.";
        String message = from + " -> " + to;
        notificationService.createNotification(userId, title, message);
        log.info("{}의 권한 변경 {}", userId, title);
    }
}
