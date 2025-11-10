package com.sprint.mission.discodeit.dto.message.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageEditRequest(
        @NotNull
        UUID uuid,
        @NotNull
        String content
) {
}
