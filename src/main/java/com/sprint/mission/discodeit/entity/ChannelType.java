package com.sprint.mission.discodeit.entity;


import lombok.Getter;

@Getter
public enum ChannelType {
    PUBLIC("public"),
    PRIVATE("private");

    private final String value;

    ChannelType(String value) {
        this.value = value;
    }

}
