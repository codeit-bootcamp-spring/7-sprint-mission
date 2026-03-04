package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationResponseDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.global.exception.discodietException.notification.NotificationForbiddenException;
import com.sprint.mission.discodeit.global.exception.discodietException.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CacheManager cacheManager;

    @CacheEvict(value = "notifications", key = "#receiverId")
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


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createMultipleNotification(List<UUID> receiverIds, String title, String content) {
        List<Notification> notificationList = receiverIds.stream().map(
                receiverId -> {
                    Notification notification = Notification.builder()
                            .title(title)
                            .content(content)
                            .receiverId(receiverId)
                            .build();
                    return notification;
                }
        ).toList();

        notificationRepository.saveAll(notificationList);
        evictCache(receiverIds);
    }

    @Cacheable(value = "notifications", key = "#receiverId")
    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getAllNotificationsByReceiverId(UUID receiverId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId);
        return notifications.stream()
                .map(notificationMapper::toResponseDto)
                .toList();
    }

    @CacheEvict(value = "notifications", key = "#receiverId")
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

    private void evictCache(List<UUID> receiverIds) {
        Cache cache = cacheManager.getCache("notifications");
        if (cache != null) {
            for (UUID receiverId : receiverIds) {
                cache.evict(receiverId);
                log.debug("알림 캐시 제거 : {} ", receiverId);
            }
        }
    }
}
