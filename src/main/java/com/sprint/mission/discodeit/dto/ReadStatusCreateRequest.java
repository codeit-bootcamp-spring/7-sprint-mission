package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest( //all private final
                                       @NotNull
    UUID userId,
                                       @NotNull
    UUID channelId,
                                       @NotNull
    Instant lastReadAt
){
    public static ReadStatusCreateRequest from(UUID userId, UUID channelId) {
        return new ReadStatusCreateRequest(
            userId,
            channelId,
            Instant.now()
        );
    }
}