package com.sprint.mission.discodeit.application.dto.request;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {
}
