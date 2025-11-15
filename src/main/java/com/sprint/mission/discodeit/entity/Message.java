package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity{
    private final UUID authorId;
    private final UUID channelId;
    private final ReceiveType receiveType;
    private String content;
    private List<UUID> attachmentIds;

    public Message(UUID authorId, UUID channelId, ReceiveType receiveType, String content) {
        this.authorId = authorId;
        this.channelId = channelId;
        this.receiveType = receiveType;
        this.content = content;
        this.attachmentIds = new ArrayList<>();
    }

    public void update(String content) {
        boolean flag = false;

        if (content != null && !content.equals(this.content)) {
            this.content = content;
            flag = true;
        }
        if (flag) {
            this.setUpdatedAt();
        }
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
                ", authorId=" + authorId +
                ", channelId=" + channelId +
                str +
                '}';
    }
}
