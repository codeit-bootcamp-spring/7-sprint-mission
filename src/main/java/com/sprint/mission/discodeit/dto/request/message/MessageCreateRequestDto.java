package com.sprint.mission.discodeit.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MessageCreateRequestDto(
        @NotBlank
        @Size(max = 1000)
        String content,

        @NotNull
        UUID authorId,

        @NotNull
        UUID channelId) {
}
