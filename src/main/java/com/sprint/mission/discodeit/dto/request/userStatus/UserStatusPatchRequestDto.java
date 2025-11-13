package com.sprint.mission.discodeit.dto.request.userStatus;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record UserStatusPatchRequestDto(
        @NotBlank
        Instant newLastActiveAt
) {
}
