package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {

    private String content;
    private UUID senderId;

    public Message(User user,String content) {
        UUID senderId = user.getId();
        this.content = content;
        this.senderId = senderId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public UUID getSenderId() {
        return senderId;
    }
}
