package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.notification.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> getAllByReceiverId(UUID receiverId);
    void delete(UUID notificationId, UUID requesterId);
    NotificationDto create(UUID receiverId, String title, String content);
}
