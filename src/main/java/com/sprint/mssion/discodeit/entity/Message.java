package com.sprint.mssion.discodeit.entity;

import java.util.UUID;

public class Message {
    private Common common;
    private String message;
    private UUID channelId;
    private UUID userId;

    public Message(String message, UUID channelId, UUID userId) {
        this.message = message;
        this.channelId = channelId;
        this.userId = userId;
        this.common = new Common();
    }

    public Common getCommon() {
        return common;
    }

    public String getMessage() {
        return message;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "common=" + common +
                ", message='" + message + '\'' +
                ", channelId=" + channelId +
                ", userId=" + userId +
                '}';
    }
}
