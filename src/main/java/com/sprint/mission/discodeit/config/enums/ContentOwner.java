package com.sprint.mission.discodeit.config.enums;

import com.sprint.mission.discodeit.data.DataPersistenceManager;

import java.util.UUID;

public enum ContentOwner {
    CHANNEL_MESSAGE("channel_message_files"),
    DIRECT_MESSAGE("direct_message_files"),
    USER("user_files");

    private static final String ROOT_STORAGE_PATH = DataPersistenceManager.ROOT_PATH;

    private final String desc;

    ContentOwner(String desc) {
        this.desc = desc;
    }

    public String getBaseStoragePath() {
        return ROOT_STORAGE_PATH + this.desc + "/";
    }

    public String getOwnerSpecificPath(UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("경로 생성을 위한 Owner ID는 null일 수 없습니다.");
        }

        return getBaseStoragePath() + ownerId.toString() + "/";
    }

    public String getDesc() {
        return this.desc;
    }
}
