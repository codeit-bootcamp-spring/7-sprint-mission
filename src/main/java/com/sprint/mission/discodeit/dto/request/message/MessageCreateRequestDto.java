package com.sprint.mission.discodeit.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequestDto(
        @NotBlank
        String content,

        @NotNull
        UUID authorId,

        @NotNull
        UUID channelId) {
}
