package com.sprint.mission.discodeit.entity.enums;

import lombok.Getter;

@Getter
public enum UserStatusType {
    ONLINE(true),
    OFFLINE(false);

    private final boolean isOnline;

    UserStatusType(boolean isOnline) {
        this.isOnline = isOnline;
    }

}
