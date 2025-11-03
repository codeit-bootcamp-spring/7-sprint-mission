package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity{
    private final UUID senderId;
    private final UUID receiverId;
    private final ReceiveType receiveType;
    private String content;
    private List<UUID> attachmentIds;

    public Message(UUID senderId, UUID receiverId, ReceiveType receiveType, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiveType = receiveType;
        this.content = content;
        this.attachmentIds = new ArrayList<>();
    }

    public void setContent(String content) {
        this.setUpdatedAt();
        this.content = content;
    }

    public void addAttachmentId(UUID id) {
        this.setUpdatedAt();
        this.attachmentIds.add(id);
    }

    public void deleteAttachmentId(UUID id) {
        this.setUpdatedAt();
        this.attachmentIds.remove(id);
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
