package com.sprint.mission.discodeit.repository.sse;

import java.util.UUID;

public record SseMessage(
        UUID eventId,
        String eventName,
        Object data
) {
}
