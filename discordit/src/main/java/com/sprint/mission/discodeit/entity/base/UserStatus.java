package com.sprint.mission.discodeit.entity.base;

import java.util.UUID;

public class UserStatus extends BaseEntity {
    public User user;

    public UserStatus(User user) {
        this.user = user;
    }
}
