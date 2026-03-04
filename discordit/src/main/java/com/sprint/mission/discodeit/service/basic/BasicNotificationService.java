package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.notification.NotificationDto;
import com.sprint.mission.discodeit.dto.mapper.NotificationMapper;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDto> getAllByUserId(UUID userId) {
        return notificationRepository.findAllByReceiver(
                        userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(userId))
                ).stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    public List<NotificationDto> getAll() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    public void confirm(UUID requestedUserId, UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        if (!requestedUserId.equals(notification.getReceiver().getId()))
            throw new AccessDeniedException("권한이 없는 요청입니다.");
        notificationRepository.delete(notification);
    }
}
