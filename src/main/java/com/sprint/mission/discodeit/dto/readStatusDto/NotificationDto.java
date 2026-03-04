package com.sprint.mission.discodeit.dto.readStatusDto;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        Instant createAt,
        UUID receivedId,
        String title,
        String content
) {
}
