package com.sprint.mission.discodeit.dto.entity.userStatus.request;

import jakarta.validation.constraints.NotNull;

public record UserStatusCreateRequest(
        @NotNull(message = "유저 id는 필수입니다.")
        String userId
) {
}
