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

    private UUID userId;
    private Instant lastSeenAt;

    public UserStatus(UUID userId, Instant lastSeenAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.lastSeenAt = lastSeenAt;
    }

    public void updateLastSeen(Instant when) {
        this.lastSeenAt = when;
        this.updatedAt = Instant.now();
    }

    /** 마지막 접속이 5분 이내면 온라인 */
    public boolean isOnlineNow() {
        return lastSeenAt != null
                && Duration.between(lastSeenAt, Instant.now()).toMinutes() < 5;
    }
}
