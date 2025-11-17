package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public record Dto_ReadStatus( //all private final
        //@NotBlank(message = "id is mandatory")
        UUID userId,
        //@NotBlank(message = "channelId is mandatory")
        UUID channelId,
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