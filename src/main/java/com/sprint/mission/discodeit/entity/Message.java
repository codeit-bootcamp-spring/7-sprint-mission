package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends DefEntity{
    private UUID senderId;
    private UUID channelId;
    private String content;

    public Message(UUID senderId, UUID channelId, String content) {
        this.senderId = senderId;
        this.channelId = channelId;
        this.content = content;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        touch(); // 수정 시간 갱신
    }


    public UUID getSenderId() {
        return senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

}
