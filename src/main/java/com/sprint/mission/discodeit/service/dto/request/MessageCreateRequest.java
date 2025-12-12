package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequest(
        @NotNull
        String content,
        @NotNull
        UUID authorId,
        @NotNull
        UUID channelId
){}
