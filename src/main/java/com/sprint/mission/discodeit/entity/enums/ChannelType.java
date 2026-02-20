package com.sprint.mission.discodeit.entity.enums;

import lombok.Getter;

@Getter
public enum ChannelType {
    PUBLIC("public"),PRIVATE("private");
//    TEXT("텍스트"), VOICE("음성"),
//    FORUM("포럼");

    private final String descType;

    ChannelType(String descType) {
        this.descType = descType;
    }

}