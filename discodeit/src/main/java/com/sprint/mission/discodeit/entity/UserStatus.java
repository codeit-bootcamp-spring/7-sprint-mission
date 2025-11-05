package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
    }

    public void LastUpdateUserState(){
        this.updatedAt = Instant.now();
    }

    public boolean isOnline(){
        if (this.updatedAt == null) { return false; }
        Instant now = Instant.now();
        Duration duration = Duration.between(this.updatedAt, now);

        return duration.toMinutes() < 5;

    }
}
