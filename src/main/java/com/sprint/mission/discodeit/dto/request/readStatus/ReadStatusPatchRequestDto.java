package com.sprint.mission.discodeit.dto.request.readStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadStatusPatchRequestDto(

        @NotNull(message = "ReadStatus 새로운 마지막 읽은 시간은 필수값입니다")
        Instant newLastReadAt
) {
}
