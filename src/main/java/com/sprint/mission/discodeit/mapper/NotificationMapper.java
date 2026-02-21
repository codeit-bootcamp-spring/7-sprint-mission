package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readStatusDto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;

public class NotificationMapper {

    public static NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getCreatedAt(),
                notification.getUser().getId(),
                notification.getTitle(),
                notification.getContent()
        );
    }
}
