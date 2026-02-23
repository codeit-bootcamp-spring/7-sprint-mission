package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

    private final ReadStatusRepository readStatusRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageCreatedEvent event) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllEnabledByChannelId(event.getChannelId());

        List<Notification> notifications = readStatuses.stream()
                .map(ReadStatus::getUser)
                .filter(user -> !user.getId().equals(event.getSenderId())) // 발신자 제외
                .map(user -> new Notification(
                        event.getSenderName() + " (#" + event.getChannelName() + ")",
                        event.getContent(),
                        user
                ))
                .toList();

        if (!notifications.isEmpty()) {
            notificationRepository.saveAll(notifications);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RoleUpdatedEvent event) {
        User receiver = userRepository.findById(event.getReceiverId()).orElseThrow();

        Notification notification = new Notification(
                "권한이 변경되었습니다.",
                event.getOldRole().name() + " -> " + event.getNewRole().name(),
                receiver
        );
        notificationRepository.save(notification);
    }
}
