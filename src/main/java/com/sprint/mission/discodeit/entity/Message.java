package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message extends BaseEntity {
    private final User author;
    private String content;

    private final User receiver;
    private final Channel channel;

    private final MessageType type;

    public Message(User author, User receiver, String content) {
        super();
        this.author = author;
        this.receiver = receiver;
        this.content = content;
        this.type = MessageType.DIRECT;
        this.channel = null;
    }   // DM

    public Message(User author, Channel channel, String content) {
        super();
        this.author = author;
        this.content = content;
        this.channel = channel;
        this.type = MessageType.CHANNEL;
        this.receiver = null;
    }   // CM

    public void updateContent(String content) {
        this.content = content;
        updateTimestamp();
    }
}



