package com.sprint.mission.discodeit.service.sse;

import java.time.Instant;
import java.util.UUID;

public record SseMessage(
        UUID id,
        UUID receiverId,
        String eventName,
        Object data,
        Instant createdAt
) {
}