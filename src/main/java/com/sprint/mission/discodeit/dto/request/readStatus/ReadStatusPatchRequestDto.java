package com.sprint.mission.discodeit.dto.request.readStatus;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record ReadStatusPatchRequestDto(
        @NotBlank
        Instant newLastReadAt
) {
}
