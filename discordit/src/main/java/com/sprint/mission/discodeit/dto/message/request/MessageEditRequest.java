package com.sprint.mission.discodeit.dto.message.request;

import jakarta.validation.constraints.NotNull;

public record MessageEditRequest(
        @NotNull
        String newContent
) {
}
