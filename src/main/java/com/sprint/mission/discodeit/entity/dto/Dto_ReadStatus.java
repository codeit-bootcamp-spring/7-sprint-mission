package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record Dto_ReadStatus( //all private final
        @NotNull
        UUID userId,
        @NotNull
        UUID channelId,
        @NotNull
        Instant lastReadAt
){
    public static Dto_ReadStatus from(UUID userId, UUID channelId) {
        return new Dto_ReadStatus(
            userId,
            channelId,
            Instant.now()
        );
    }
}