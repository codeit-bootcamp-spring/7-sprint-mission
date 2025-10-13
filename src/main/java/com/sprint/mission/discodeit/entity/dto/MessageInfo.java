package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public class MessageInfo {

    private UUID id;
    private String author;
    private String receiver;
    private String content;
    private double createdAt;
    private double updatedAt;

    public MessageInfo(Message message) {
        this.id = message.getId();
        this.author = message.getAuthor().getUserName();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
        this.receiver = message.getType() == Message.MessageType.DIRECT ?
                message.getReceiver().getUserName() :
                message.getChannel().getChannelName();
    }

    public UUID getId() { return id; }
    public String getAuthor() {
        return author;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getContent() {
        return content;
    }
    public double getCreatedAt() {
        return createdAt;
    }
    public double getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {

        String editSign = createdAt == updatedAt ? "" : " (수정됨)";
        return author + "   " + createdAt + "\n" + content  + editSign;
    }
}
