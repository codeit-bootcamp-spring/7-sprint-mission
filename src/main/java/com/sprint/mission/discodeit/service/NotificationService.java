package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.dto.response.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(UUID receiverId){
        List<Notification> result = notificationRepository.findNotificationsByReceiverId(receiverId);
        return result.stream().map(NotificationDto::from).toList();
    }

    @Transactional
    public void checkNotification(UUID notificationId, UUID requesterId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        ErrorCode.NOTIFICATION_NOT_FOUND,
                        new HashMap<>()
                ));

        if (!notification.getReceiver().getId().equals(requesterId)) {
            throw new AccessDeniedException("본인의 알림만 확인할 수 있습니다.");
        }

        notification.markAsRead();
    }
}
