package com.sprint.mission.discodeit.dto.request.userStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusPatchRequestDto(
        @NotNull(message = "UserStatus newLastActiveAt")
        Instant newLastActiveAt
) {
}
