package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class NotificationDto {
    private UUID id;
    private Instant createdAt;
    private UUID receiverId;
    private String title;
    private String content;

    public static NotificationDto from(Notification notification){
        return new NotificationDto(
                notification.getId(),
                notification.getCreatedAt(),
                notification.getReceiver().getId(),
                notification.getTitle(),
                notification.getContent()
        );
    }
}
