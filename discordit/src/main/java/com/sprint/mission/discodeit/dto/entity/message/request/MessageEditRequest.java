package com.sprint.mission.discodeit.dto.entity.message.request;

import jakarta.validation.constraints.NotNull;

public record MessageEditRequest(
        @NotNull(message = "수정 메세지는 필수입니다.")
        String newContent
) {
}
