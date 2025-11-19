package com.sprint.mission.discodeit.dto.userStatus.request;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {
}
