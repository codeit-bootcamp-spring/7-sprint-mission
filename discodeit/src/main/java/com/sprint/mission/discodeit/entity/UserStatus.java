package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private UUID userId;
    private Instant lastActiveAt;

    private boolean isOnline;

    public UserStatus(UUID userId, Instant lastActiveAt, boolean isOnline) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        //
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;

        this.isOnline = isOnline;
    }

    public void update(Instant lastActiveAt) {
        boolean anyValueUpdated = false;
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public boolean isRecentlyActive() {
        Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        return lastActiveAt != null && lastActiveAt.isAfter(fiveMinutesAgo);
    }

    public String getStatus() {
        return isOnline ? "ONLINE" : "OFFLINE";
    }
}
