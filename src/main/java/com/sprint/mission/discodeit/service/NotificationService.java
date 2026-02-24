package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createForMessageCreated (MessageCreatedEvent event);

    List<NotificationDto> getMyNotifications(UUID userId);
}
