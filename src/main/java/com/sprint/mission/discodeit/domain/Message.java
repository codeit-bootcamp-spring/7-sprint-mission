package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message {

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;

    private final UUID channelId;
    private final UUID authorId;

    private String content;
    private List<UUID> attachmentIds;


    public Message(UUID authorId, String content, UUID channelId, List<UUID> attachmentIds) {
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.updatedAt=Instant.now();
        this.authorId = authorId;
        this.content = content;
        this.channelId=channelId;
        this.attachmentIds = attachmentIds;
    }

    public void updateContent(String newContent){
        this.content=newContent;
    }
}
