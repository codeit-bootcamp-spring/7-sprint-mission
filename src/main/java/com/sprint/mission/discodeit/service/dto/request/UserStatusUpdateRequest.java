package com.sprint.mission.discodeit.service.dto.request;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {
}
