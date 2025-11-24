package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCteateRequest( //all private final
                                       @NotNull
        UUID userId,
                                       @NotNull
        UUID channelId,
                                       @NotNull
        Instant lastReadAt
){
    public static ReadStatusCteateRequest from(UUID userId, UUID channelId) {
        return new ReadStatusCteateRequest(
            userId,
            channelId,
            Instant.now()
        );
    }
}