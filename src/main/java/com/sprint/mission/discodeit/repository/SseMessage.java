package com.sprint.mission.discodeit.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class SseMessage {
    private final UUID id;
    private final UUID receiverId;
    private final String eventName;
    private final Object data;
    private final Instant createdAt;
}
