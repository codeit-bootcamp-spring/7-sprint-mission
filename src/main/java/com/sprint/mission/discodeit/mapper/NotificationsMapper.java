package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.dto_Neo.NotificationDto;
import com.sprint.mission.discodeit.entity.Notifications;
import javax.management.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationsMapper {

    public NotificationDto toDto(Notifications notification) {
        return NotificationDto.builder()
            .id(notification.getId())
            .createdAt(notification.getCreatedAt())
            .receiverId(notification.getReceiverId())
            .title(notification.getTitle())
            .content(notification.getContent())
            .build();
    }
}
