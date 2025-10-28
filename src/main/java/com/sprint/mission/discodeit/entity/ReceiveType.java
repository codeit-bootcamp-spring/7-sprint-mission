package com.sprint.mission.discodeit.entity;

public enum ReceiveType {
    USER("유저 메시지"), CHANNEL("채널 메시지");

    private final String desc;

    ReceiveType(String desc){
        this.desc = desc;
    }
}
