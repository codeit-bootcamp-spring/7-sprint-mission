package com.sprint.mission.discodeit.vo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Message {
    private final UUID senderId;
    private final String content;


    public Message(UUID senderId, String content) {
        this.content = content;
        this.senderId = senderId;
    }
}
