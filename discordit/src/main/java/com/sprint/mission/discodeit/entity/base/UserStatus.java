package com.sprint.mission.discodeit.entity.base;

import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.util.UUID;

public class UserStatus extends BaseEntity {
    public User user;

    public UserStatus(User user) {
        this.user = user;
    }

    public boolean isOnline() {
        return user.getOnlineStatus() == OnlineStatus.ONLINE;
    }
}
