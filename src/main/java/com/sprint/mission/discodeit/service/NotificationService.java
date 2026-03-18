package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusDto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    void createNotification(User user, String title, String content);

    void createNotifications(List<Notification> notifications);

    List<NotificationDto> findAllNotifications(UUID userId);

    void deleteNotification(UUID id, UUID userId);
}
