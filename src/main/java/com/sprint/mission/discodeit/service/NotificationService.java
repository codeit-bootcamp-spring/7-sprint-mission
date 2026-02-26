package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.event.BinaryContentUploadFailedEvent;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createForMessageCreated (MessageCreatedEvent event);

    void createForRoleUpdate(RoleUpdatedEvent event);

    void createForFailedUpload(BinaryContentUploadFailedEvent event);

    List<NotificationDto> getMyNotifications(UUID userId);

    void deleteNotification(UUID notificationId, UUID userId);

}
