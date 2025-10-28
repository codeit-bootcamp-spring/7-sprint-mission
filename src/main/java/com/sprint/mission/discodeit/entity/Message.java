package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.Common;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID authorId;
    private UUID channelId;
    private Instant time;
    private String content;

    public Message() {
    }


    public  Message(String content,UUID channelId,UUID authorId ){
        this.authorId = authorId;
        this.channelId = channelId;
        this.time = Instant.now();
        this.content = content;
    }

/*    public UUID getSender() {
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

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }*/

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
            this.setUpdatedAt(Instant.ofEpochSecond(Instant.now().getEpochSecond()));
        }
    }
}
