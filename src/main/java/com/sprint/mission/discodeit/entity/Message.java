package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{
    public final UUID senderId;
    public final UUID receiverId;
    public String content;

    public Message(UUID senderId, UUID receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    public String getContents() {
        return content;
    }
    
    //코드 추가 필요
    public void setContents(String content) {
        this.setUpdatedAt();
        this.content = content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    @Override
    public String toString() {
        String str = super.toString();
        return "Message{" +
                "contents='" + content + '\'' +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                str +
                '}';
    }
}
