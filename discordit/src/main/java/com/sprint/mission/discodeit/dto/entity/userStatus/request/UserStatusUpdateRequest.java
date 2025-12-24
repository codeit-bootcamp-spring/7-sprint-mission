package com.sprint.mission.discodeit.dto.entity.userStatus.request;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {
}
