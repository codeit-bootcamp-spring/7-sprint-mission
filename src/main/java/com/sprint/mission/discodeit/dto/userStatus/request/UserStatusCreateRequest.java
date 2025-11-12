package com.sprint.mission.discodeit.dto.userStatus.request;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId,
        Instant lastActiveAt
) {
}
