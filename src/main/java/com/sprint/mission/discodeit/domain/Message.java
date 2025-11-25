package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message {

    private String id;
    private Instant createdAt;
    private Instant updatedAt;

    private String channelId;
    private String userId;

    private String content;
    private List<String> attachmentIds;


    public Message(String userId, String content, String channelId, List<String> attachmentIds) {
        this.id=UUID.randomUUID().toString();

        this.userId = userId;
        this.content = content;
        this.channelId=channelId;
        this.attachmentIds = attachmentIds;
    }

    public void updateContent(String newContent){
        this.content=newContent;
    }
}
