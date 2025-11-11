package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserStatus extends BaseEntity{
    private final UUID userId;
    private Instant lastActiveAt;

    public void update(Instant lastActiveAt){
        if(lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)){
            this.lastActiveAt = lastActiveAt;
            this.setUpdatedAt();
        }
    }

    public boolean isOnline() {
        Instant lastLogin = getUpdatedAt();
        Instant now = Instant.now();
        Duration duration = Duration.between(lastLogin, now);

        if (duration.toMinutes() < 5) return true; //접속이 5분이내이면 현재 접속중인 유저로 간주
        else return false;
    }
}
