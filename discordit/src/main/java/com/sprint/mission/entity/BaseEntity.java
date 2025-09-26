package com.sprint.mission.entity;

import java.time.Instant;
import java.util.UUID;

public class BaseEntity {
    protected UUID uuid;
    protected Long createdAt;
    protected Long updatedAt;

    BaseEntity () {
        this.uuid = UUID.randomUUID();
        this.createdAt = getUnixTimestamp();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    protected static long getUnixTimestamp() {
        return Instant.now().getEpochSecond();
    }

}
