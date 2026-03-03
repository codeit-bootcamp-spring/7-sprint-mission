package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.request.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class NotificationStorage {

    public ConcurrentHashMap<UUID, NotificationDto> notifications = new ConcurrentHashMap<>();

    public NotificationDto get(UUID notificationId) {

        return notifications.get(notificationId);
    }

    @Transactional
    @CacheEvict(value = "notifications",allEntries = true)
    public void save(NotificationDto notificationDto) {
        notifications.put(notificationDto.id(), notificationDto);
    }

    @Transactional
    @CacheEvict(value = "notifications",allEntries = true)
    public void delete(UUID id) {
        notifications.remove(id);
    }

    public List<NotificationDto> getAll() {
        return new ArrayList<>(notifications.values());
    }

    @Transactional
    @CachePut(value= "notifications", key = "#userId")
    public List<NotificationDto> getUserNotification(UUID userId) {
        List<NotificationDto> result = getAll().stream().filter(
                x ->
                        x.receiverId().equals(userId)
        ).toList();
        return result;
    }

}
