package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID sender;
    private UUID receiver;
    private Long time;
    private String content;

    public Message() {
    }


    public  Message(UUID send,UUID receiver,String content ){
        this.sender = send;
        this.receiver = receiver;
        this.time = System.currentTimeMillis();
        this.content = content;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
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
                "발신자 UUID  =" + sender+
                ", 수신자 UUID =" + receiver+
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
