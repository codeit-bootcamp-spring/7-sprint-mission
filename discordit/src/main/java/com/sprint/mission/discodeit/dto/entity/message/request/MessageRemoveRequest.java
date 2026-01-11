package com.sprint.mission.discodeit.dto.entity.message.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageRemoveRequest(
        @NotNull(message = "메세지 id는 필수입니다.")
        UUID id
) {
}
