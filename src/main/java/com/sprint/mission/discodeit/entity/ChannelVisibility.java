package com.sprint.mission.discodeit.entity;

public enum ChannelVisibility {
    PUBLIC("공개 채널"), PRIVATE("비공개 채널");

    private final String desc;

    ChannelVisibility(String desc){
        this.desc = desc;
    }
}
