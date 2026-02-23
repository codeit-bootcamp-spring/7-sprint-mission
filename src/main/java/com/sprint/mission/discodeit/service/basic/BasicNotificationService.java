package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.response.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<NotificationDto> findAll(DiscodeitUserDetails userDetails) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userDetails.getUserDto().id());

        return notifications.stream()
                .map(notification -> new NotificationDto(
                        notification.getId(),
                        notification.getCreatedAt(),
                        notification.getUserId(),
                        notification.getTitle(),
                        notification.getContent()
                ))
                .toList();
    }

    public void delete(UUID userId, UUID notificationId) {

        // 알림이 없는 경우 404 NOT FOUND 예외 발생
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND));

        // 인가되지 않은 요청은 403 FORBIDDEN 예외 발생
        if (!notification.getUserId().equals(userId)) {
            throw new AuthorizationDeniedException("요청자 본인의 알림에 대해서만 요청이 가능합니다.");
        }

        notificationRepository.deleteById(notificationId);

        log.info("알림을 확인하였습니다. {} {}", userId, notificationId);
    }
}