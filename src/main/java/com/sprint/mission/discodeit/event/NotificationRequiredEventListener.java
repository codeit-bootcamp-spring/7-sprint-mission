package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {
    private final NotificationService notificationService;
//    @TransactionalEventListener
//    public void on(MessageCreatedEvent event) {...}

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void on(RoleUpdatedEvent event) {
        UUID userId = event.getUserId();
        Role from = event.getFrom();
        Role to = event.getTo();
        String title = "권한이 변경되었습니다.";
        String message = from + " -> " + to;
        log.info("{}의 권한 변경 {}", userId, title);
        notificationService.createNotification(userId, title, message);
    }
}
