package com.sprint.mssion.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Common common;
    private String content;
    private final UUID channelId;
    private final UUID userId;

    public Message(String content, UUID channelId, UUID userId) {
        this.content = content;
        this.channelId = channelId;
        this.userId = userId;
        this.common = new Common();
    }

    public Common getCommon() {
        return common;
    }

    public String getContent() {
        return content;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "common=" + common +
                ", content='" + content + '\'' +
                ", channelId=" + channelId +
                ", userId=" + userId +
                '}';
    }
}
