package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID authorId;
    private UUID channelId;
    private Long time;
    private String content;

    public Message() {
    }


    public  Message(UUID channelId,UUID authorId,String content ){
        this.authorId = authorId;
        this.channelId = channelId;
        this.time = System.currentTimeMillis();
        this.content = content;
    }

    public UUID getSender() {
        return authorId;
    }

    public void setSender(UUID sender) {
        this.authorId = sender;
    }

    public UUID getReceiver() {
        return channelId;
    }

    public void setReceiver(UUID receiver) {
        this.channelId = receiver;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "채널 UUID =" + channelId+
                ",발신자 UUID  =" + authorId+
                ", 생성시간 =" + time +
                ",  내용 ='" + content + '\'' +
                '}';
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.setUpdatedAt(Instant.now().getEpochSecond());
        }
    }
}
