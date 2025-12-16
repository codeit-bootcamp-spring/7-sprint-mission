package com.sprint.mission.discodeit.dto.request.userStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusPatchRequestDto(

        @NotNull(message = "마지막으로 활성화 된 시간은 필수값입니다")
        Instant newLastActiveAt
) {
}
