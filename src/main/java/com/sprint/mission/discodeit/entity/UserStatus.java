package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import lombok.Getter;

import java.io.Serial;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
public class UserStatus extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    private Instant updateAt;
    private final UUID userId;
    private Instant loginAt;

    public UserStatus(UUID userId, Instant loginAt) {
        this.updateAt = Instant.now();
        this.userId = userId;
        this.loginAt = loginAt;
    }

    public void update(Instant loginAt) {
        boolean isUpdate = false;
        if(this.loginAt != null && !this.loginAt.equals(loginAt)) {
            this.loginAt = loginAt;
            isUpdate = true;
        }

        if(isUpdate) updateAt = Instant.now();
    }

    public UserStatusType isOnline() {
        System.out.println("현재 시간은: " + Instant.now());
        if(Duration.between(loginAt, Instant.now()).toSeconds() <= 300) {
            return UserStatusType.ONLINE;
        }
        return UserStatusType.OFFLINE;
    }


}
