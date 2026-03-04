package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.dto_Neo.S3UploadFailedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.MessageCreatedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.NotificationDto;
import com.sprint.mission.discodeit.dto.dto_Neo.RoleUpdatedEvent;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.util.List;
import java.util.UUID;

public interface InterfaceNotificationsService {
    List<NotificationDto> getNotifications(DiscodeitUserDetails userDetails);

    void deleteNotification(UUID notificationId, DiscodeitUserDetails userDetails);

    void saveMessageCreatedEvent(MessageCreatedEvent event);
    void saveRoleUpdateEvent(RoleUpdatedEvent event);
    void saveBinaryContentStorageErrorEvent(S3UploadFailedEvent event);
}
