package com.sprint.mission.discodeit.dto.request.userstatus;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequestDto(
        Instant lastActiveAt) {
}
