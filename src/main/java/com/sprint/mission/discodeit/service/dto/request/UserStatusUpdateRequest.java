package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequest(
        @NotNull Instant newLastActiveAt
) {
}
