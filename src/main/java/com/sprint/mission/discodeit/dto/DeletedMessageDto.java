package com.sprint.mission.discodeit.dto;

public class DeletedMessageDto {
    private final String senderName;
    private final String content;
    private final Long DeletedTime;

    public DeletedMessageDto(String senderName, String content) {
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
