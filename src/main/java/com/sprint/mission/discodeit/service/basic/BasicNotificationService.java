package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.notification.NotificationAccessDeniedException;
import com.sprint.mission.discodeit.common.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.response.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BasicNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Override
    @Cacheable(cacheNames = "notificationListCache", key = "#receiverId")
    public List<NotificationDto> getAllByReceiverId(UUID receiverId) {
        return notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(notification -> notificationMapper.toDto(notification))
                .toList();
    }

    @CacheEvict(cacheNames = "notificationListCache", key = "#requesterId")
    @Transactional
    @Override
    public void delete(UUID notificationId, UUID requesterId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        UUID receiverId = notification.getReceiver().getId();

        if (!receiverId.equals(requesterId)) {
            throw new NotificationAccessDeniedException(notificationId);
        }

        notificationRepository.delete(notification);
    }

    @CacheEvict(cacheNames = "notificationListCache", key = "#receiverId")
    @Transactional
    @Override
    public NotificationDto create(UUID receiverId, String title, String content) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException(receiverId));

        Notification notification = new Notification(
                receiver,
                title,
                content
        );

        Notification saved = notificationRepository.save(notification);

        return notificationMapper.toDto(saved);
    }
}
