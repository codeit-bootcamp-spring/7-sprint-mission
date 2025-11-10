package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message implements Serializable {

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;

    private final UUID channelId;
    private final UUID senderId;

    private String content;
    private UUID image;


    public Message(UUID senderId, String content, UUID channelId, UUID image) {
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.updatedAt=Instant.now();
        this.senderId = senderId;
        this.content = content;
        this.channelId=channelId;
        this.image= image;
    }

    public void updateContent(String newContent){
        this.content=newContent;
    }

    public void updateImage(UUID newImage){
        this.image=newImage;
    }
}
