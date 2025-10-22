package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    TEXT("텍스트"), VOICE("음성"),
    FORUM("포럼");

    private final String descType;

    ChannelType(String descType) {
        this.descType = descType;
    }

    public String getDescType() {
        return descType;
    }

}