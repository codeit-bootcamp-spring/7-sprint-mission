package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotNull
        UUID userId,
        @NotNull
        UUID channelId,
        @NotNull
        Instant lastReadAt
) {
}
