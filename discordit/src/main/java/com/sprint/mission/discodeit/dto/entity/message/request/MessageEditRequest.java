package com.sprint.mission.discodeit.dto.entity.message.request;

import jakarta.validation.constraints.NotNull;

public record MessageEditRequest(
        @NotNull
        String newContent
) {
}
