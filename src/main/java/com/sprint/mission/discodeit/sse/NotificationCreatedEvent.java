package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.service.dto.response.NotificationDto;

import java.util.UUID;

public record NotificationCreatedEvent(
        UUID receiverId,
        NotificationDto notificationDto
) {
}