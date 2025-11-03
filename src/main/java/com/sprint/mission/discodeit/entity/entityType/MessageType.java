package com.sprint.mission.discodeit.entity.entityType;

import lombok.Getter;

@Getter
public enum MessageType {
    CHANNEL("채널메시지"), DIRECT("개인메시지");
    private final String desc;
    MessageType(String description) {
        this.desc = description;
    }
}   // CHANNEL, DIRECT