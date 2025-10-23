package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{
    private final UUID senderId;
    private final UUID receiverId;
    private final ReceiveType receiveType;
    private String content;

    public enum ReceiveType{
        USER("유저"), CHANNEL("채널");

        private final String desc;

        ReceiveType(String desc) {
            this.desc = desc;
        }
    }

    public Message(UUID senderId, UUID receiverId, ReceiveType receiveType, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiveType = receiveType;
        this.content = content;
    }

    public String getContents() {
        return content;
    }

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
