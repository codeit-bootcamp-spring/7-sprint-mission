package com.sprint.mission.discodeit.dto.entity.user.request;

import jakarta.validation.constraints.NotNull;

public record UserGetByUserIdRequest(
        @NotNull(message = "닉네임은 필수입니다.")
        String username
) {
}
