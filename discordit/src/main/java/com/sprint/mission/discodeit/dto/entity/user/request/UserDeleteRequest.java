package com.sprint.mission.discodeit.dto.entity.user.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDeleteRequest(
        @NotNull(message = "유저 id는 필수입니다.")
        UUID id
) {
}
