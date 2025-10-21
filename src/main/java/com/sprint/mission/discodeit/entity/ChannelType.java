package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    MESSAGE("메시지 채널"), VOICE("음성 채널");

    private final String desc;

    ChannelType(String desc){
        this.desc = desc;
    }
}
