//package com.sprint.mission.discodeit.event.listener;
//
//import com.sprint.mission.discodeit.entity.Notification;
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.event.MessageCreatedEvent;
//import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.repository.NotificationRepository;
//import com.sprint.mission.discodeit.repository.ReadStatusRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.CacheManager;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.event.TransactionPhase;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class NotificationRequiredEventListener {
//
//    private final ReadStatusRepository readStatusRepository;
//    private final NotificationRepository notificationRepository;
//    private final UserRepository userRepository;
//    private final CacheManager cacheManager;
//
//    @Async("taskExecutor")
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void onMessageCreated(MessageCreatedEvent event) {
//        List<ReadStatus> subscriptions
//                = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(
//                event.channelId());
//
//        List<Notification> notifications = subscriptions.stream()
//                .map(ReadStatus::getUser)
//                .filter(user -> !user.getUsername().equals(event.authorName()))
//                .map(user -> Notification.builder()
//                        .user(user)
//                        .title(String.format("%s (#%s)", event.authorName(), event.channelName()))
//                        .content(event.content())
//                        .build())
//                .toList();
//        notificationRepository.saveAll(notifications);
//
//        if (cacheManager.getCache("notification") != null) {
//            notifications.forEach(n -> {
//                Objects.requireNonNull(cacheManager.getCache("notification")).evict(n.getUser().getId());
//                log.debug("메시지 알림 캐시 무효화: userId={}", n.getUser().getId());
//            });
//        }
//    }
//
//    @Async("taskExecutor")
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void onRoleUpdated(RoleUpdatedEvent event) {
//        User user = userRepository.findById(event.userId())
//                .orElseThrow(() -> new UserNotFoundException(event.userId()));
//
//        Notification notification = Notification.builder()
//                .user(user)
//                .title("권한이 변경되었습니다.")
//                .content(String.format("%s -> %s", event.oldRole(), event.newRole()))
//                .build();
//        notificationRepository.save(notification);
//
//        if (cacheManager.getCache("notification") != null) {
//            Objects.requireNonNull(cacheManager.getCache("notification")).evict(user.getId());
//            log.info("권한 알림 캐시 무효화: userId={}", user.getId());
//        }
//    }
//}
