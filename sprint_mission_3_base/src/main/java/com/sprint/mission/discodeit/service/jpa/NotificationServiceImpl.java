package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Cacheable(cacheNames = "userNotifications", key = "#receiverId")
    public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
        return notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(NotificationDto::from)
                .toList();
    }

    @Override
    @CacheEvict(cacheNames = "userNotifications", key = "#receiverId")
    public void deleteById(UUID notificationId, UUID receiverId) {
        var notification = notificationRepository.findByIdAndReceiverId(notificationId, receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        notificationRepository.delete(notification);
    }

    @Override
    @CacheEvict(cacheNames = "userNotifications", key = "#receiver.id")
    public NotificationDto create(User receiver, String title, String content) {
        Notification notification = notificationRepository.save(new Notification(receiver, title, content));
        NotificationDto dto = NotificationDto.from(notification);
        eventPublisher.publishEvent(new NotificationCreatedEvent(receiver.getId(), dto));
        return dto;
    }
}