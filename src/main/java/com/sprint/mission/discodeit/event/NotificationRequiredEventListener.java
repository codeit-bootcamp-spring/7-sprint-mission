package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.event.binaryContent.BinaryContentUploadFailedEvent;
import com.sprint.mission.discodeit.event.message.MessageCreatedEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

//@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async(value = "eventTaskExecutor")
    public void on(MessageCreatedEvent event) {
        log.info("[listener] thread={}, requestId={}",
                Thread.currentThread().getName(),
                MDC.get("requestId"));
        notificationService.createForMessageCreated(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async(value = "eventTaskExecutor")
    public void on(RoleUpdatedEvent event) {
        notificationService.createForRoleUpdate(event);
    }

    @EventListener
    @Async(value = "eventTaskExecutor")
    public void handleFailedUploadBinaryContent(BinaryContentUploadFailedEvent event) {
        notificationService.createForFailedUpload(event);
    }

}
