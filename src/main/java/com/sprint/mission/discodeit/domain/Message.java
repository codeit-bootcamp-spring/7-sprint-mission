package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message {

    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private final UUID channelId;
    private final UUID userId;

    private String content;
    private List<UUID> attachmentIds;


    public Message(UUID userId, String content, UUID channelId, List<UUID> attachmentIds) {
        this.id=UUID.randomUUID();

        this.userId = userId;
        this.content = content;
        this.channelId=channelId;
        this.attachmentIds = attachmentIds;
    }

    public void updateContent(String newContent){
        this.content=newContent;
    }
}
