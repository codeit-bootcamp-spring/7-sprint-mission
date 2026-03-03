package com.sprint.mission.discodeit.dto.entity.notification.request;

import java.time.Instant;
import java.util.UUID;

public record NotificationCreateRequest (
        Instant createdAt,
        UUID receiverId,
        String content
){
}
