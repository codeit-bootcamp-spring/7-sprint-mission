package com.sprint.mission.discodeit.dto.entity.readStatus.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadStatusGetByUserRequest(
        @NotNull(message = "아이디는 필수입니다.")
        String userId
) {
}
