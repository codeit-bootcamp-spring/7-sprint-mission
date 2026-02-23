package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.notification.NotificationResponseDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    void createNotification(UUID receiverId, String title, String content);

    void createMultipleNotification(List<UUID> receiverIds, String title, String content);

    List<NotificationResponseDto> getAllNotificationsByReceiverId(UUID receiverId);

    void deleteNotification(UUID notificationId, UUID receiverId);

}
