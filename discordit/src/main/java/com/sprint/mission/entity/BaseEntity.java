package com.sprint.mission.entity;

import java.time.Instant;
import java.util.UUID;

public class BaseEntity {
    private UUID uuid = UUID.randomUUID();
    private Long createdAt;
    private Long updatedAt;

    BaseEntity () {
        this.uuid = UUID.randomUUID();
        this.createdAt = getUnixTimestamp();
    }

    private static long getUnixTimestamp() {
        return Instant.now().getEpochSecond();
    }

}
