package com.sprint.mission.discodeit.dto.notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponseDto(
        UUID id,
        Instant createdAt,
        UUID receiverId, // 알림을 수신할 User의 id
        String title,
        String content
) {
}
