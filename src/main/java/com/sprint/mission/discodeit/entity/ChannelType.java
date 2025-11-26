package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    PUBLIC("공개 채널"), PRIVATE("비공개 채널");

    private final String desc;

    ChannelType(String desc){
        this.desc = desc;
    }
}
