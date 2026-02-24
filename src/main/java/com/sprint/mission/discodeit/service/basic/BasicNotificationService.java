package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {

    private final ReadStatusRepository readStatusRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createForMessageCreated(MessageCreatedEvent event) {
        List<UUID> targetIds = readStatusRepository.findReceiverIds(event.getChannelId(), event.getSenderId());
        String title = event.getSenderName() + " (#" + event.getChannelName() + ")";
        System.out.println("targetIDS: " + targetIds);
        List<Notification> notificationList = targetIds.stream().map(targetId -> Notification.create(targetId, title, event.getContent())).toList();
        System.out.println("NotificationList: " + notificationList);
        try {
            notificationRepository.saveAll(notificationList);
            log.info("notification 저장 성공");
        } catch (Exception e) {
            log.error("notification 저장 실패", e);
        }
    }

    @Override
    public List<NotificationDto> getMyNotifications(UUID userId) {

        List<Notification> myNotifications = notificationRepository.findAllByReceiverId(userId);
        return myNotifications.stream()
                .map(notificationMapper::toDto)
                .toList();
    }
}
