package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserStatus extends BaseUpdatableEntity{
    private final UUID userId; // 삭제 필요
    private Instant lastActiveAt;

    private User user;

    public void update(Instant lastActiveAt){
        if(lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)){
            this.lastActiveAt = lastActiveAt;
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
