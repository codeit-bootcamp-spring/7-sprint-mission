package com.sprint.mission.discodeit.dto.request.readstatus;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDto(
        @NotNull
        UUID userId,

        @NotNull
        UUID channelId,
        Instant lastReadAt) {
}
