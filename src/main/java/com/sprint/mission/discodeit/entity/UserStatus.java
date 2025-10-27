package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.enums.UserStatusType;

import java.io.Serial;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


public class UserStatus extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private Instant loginAt;
    private UserStatusType userStatusType;

    public UserStatus(UUID userId) {
        this.userId = userId;
        this.userStatusType = UserStatusType.OFFLINE;
        this.loginAt = Instant.now();
    }

    public UserStatusType isOnline() {
        Duration duration = Duration.between(loginAt, Instant.now());
        if(duration.toSeconds() <= 300) {
            return this.userStatusType = UserStatusType.ONLINE;
        }

        return this.userStatusType = UserStatusType.OFFLINE;


    }


}
