package com.sprint.mission.discodeit.dto.userStatus.request;

import com.sprint.mission.discodeit.enums.OnlineStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {
}
