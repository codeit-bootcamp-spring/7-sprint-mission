package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message implements Serializable {

    private final Instant createdAt;
    private final Instant updatedAt;

    private final UUID senderId;
    private final String content;
    private final UUID channelId;
    private final BinaryContent image;


    public Message(UUID senderId, String content, UUID channelId, BinaryContent image) {
        this.createdAt=Instant.now();
        this.updatedAt=Instant.now();
        this.senderId = senderId;
        this.content = content;
        this.channelId=channelId;
        this.image= image;
    }
}
