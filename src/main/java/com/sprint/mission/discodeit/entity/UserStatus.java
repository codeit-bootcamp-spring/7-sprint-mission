package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {

    private final UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    public void updateLastAccess(Instant newTime) {
        this.lastActiveAt = newTime;
        updateTimestamp();
    }

    // 5분이 안지났으면 온라인
    public boolean isOnline() {
        Instant expirationTime = lastActiveAt.plusSeconds(300);
        // 만료시간(업데이트시간 + 5분)이 현재시간보다 뒤에 있는가? ture : false
        return expirationTime.isAfter(Instant.now());
    }

}

