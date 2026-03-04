package com.sprint.mission.discodeit.event.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageCreatedEvent(
        Instant createdAt,
        UUID channelId,
        UUID authorId,
        String content
) {
}
