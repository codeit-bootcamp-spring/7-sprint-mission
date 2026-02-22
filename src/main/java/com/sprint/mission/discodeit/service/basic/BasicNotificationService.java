package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationResponseDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.global.exception.discodietException.notification.NotificationForbiddenException;
import com.sprint.mission.discodeit.global.exception.discodietException.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNotification(UUID receiverId, String title, String content) {
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .receiverId(receiverId)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getAllNotificationsByReceiverId(UUID receiverId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId);
        return notifications.stream()
                .map(notificationMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("#receiverId == authentication.principal.userResponseDto.id")
    public void deleteNotification(UUID notificationId, UUID receiverId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> NotificationNotFoundException.byId(notificationId));

        if (!notification.getReceiverId().equals(receiverId)) {
            throw NotificationForbiddenException.byId(notificationId);
        }

        notificationRepository.delete(notification);
    }
}
