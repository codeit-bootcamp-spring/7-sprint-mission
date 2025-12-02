package com.sprint.mission.discodeit.dto.request.readStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadStatusPatchRequestDto(

        @NotNull(message = "ReadStatus newLastReadAt")
        Instant newLastReadAt
) {
}
