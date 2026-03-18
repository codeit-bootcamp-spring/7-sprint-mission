package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readstatus.response.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDto toNotificationDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getCreatedAt(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getContent()
        );
    }
}
