package com.sprint.mission.discodeit.entity.enums;

public enum UserStatusType {
    ONLINE(true),
    OFFLINE(false);

    private final boolean isOnline;

    UserStatusType(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean getOnline() {
        return isOnline;
    }

}
