package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID channelId;
    private UUID userId;

    private String content;



    public Message(UUID userId, String content, UUID channelId, List<String> attachmentIds) {


        this.userId = userId;
        this.content = content;
        this.channelId=channelId;
//        this.attachmentIds = attachmentIds;
    }

    public void updateContent(String newContent){
        this.content=newContent;
    }
}
