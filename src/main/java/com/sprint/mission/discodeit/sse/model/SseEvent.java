package com.sprint.mission.discodeit.sse.model;


import java.time.Instant;
import java.util.UUID;

public record SseEvent (
         UUID eventId,
         UUID userId,
         String eventName,
         Object data,
         Instant createdAt
        ){
}
