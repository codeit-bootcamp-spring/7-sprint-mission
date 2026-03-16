package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> findAllByReceiverId(UUID receiverId);
    void deleteById(UUID notificationId, UUID receiverId);
    NotificationDto create(User receiver, String title, String content);
}