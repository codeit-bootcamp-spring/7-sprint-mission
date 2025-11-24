package com.sprint.mission.discodeit.service.dto.request;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        String userId,
        String channelId,
        Instant lastReadAt
) {
}
