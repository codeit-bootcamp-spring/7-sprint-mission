package com.sprint.mission.discodeit.entity;

public class DeletedMessage {


    private final String senderName;
    private final String content;
    private final Long DeletedTime;

    public DeletedMessage(String senderName, String content) {
        this.senderName = senderName;
        this.content = content;
        this.DeletedTime = System.currentTimeMillis();
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

    public Long getDeletedTime() {
        return DeletedTime;
    }
}
