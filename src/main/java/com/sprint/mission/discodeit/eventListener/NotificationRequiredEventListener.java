//package com.sprint.mission.discodeit.service.basic;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionPhase;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//import com.sprint.mission.discodeit.dto.dto_Neo.S3UploadFailedEvent;
//import com.sprint.mission.discodeit.dto.dto_Neo.MessageCreatedEvent;
//import com.sprint.mission.discodeit.dto.dto_Neo.RoleUpdatedEvent;
//import org.springframework.context.event.EventListener;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class NotificationRequiredEventListener {
//    private final NotificationsService notificationsService;
//
//    @Async("myAsync")
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void on(MessageCreatedEvent event) {
//        notificationsService.saveMessageCreatedEvent(event);
//    }
//
//    @Async("myAsync")
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void on(RoleUpdatedEvent event) {
//        notificationsService.saveRoleUpdateEvent(event);
//    }
//
//    @Async("myAsync")
//    @EventListener
//    public void on(S3UploadFailedEvent event) {
//        log.error("❎❎❎ S3UploadFailedEvent = {}", event.toString());
//        notificationsService.saveBinaryContentStorageErrorEvent(event);
//    }
//}
