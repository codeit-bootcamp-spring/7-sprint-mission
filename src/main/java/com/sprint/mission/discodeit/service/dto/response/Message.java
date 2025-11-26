package com.sprint.mission.discodeit.service.dto.response;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
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
    private final List<BinaryContent> attachments = new ArrayList<>();


    public Message(UUID userId, String content, UUID channelId) {
        this.userId = userId;
        this.content = content;
        this.channelId = channelId;
    }

    public void addAttachment(BinaryContent content) {
        if (!this.attachments.contains(content)) {
            this.attachments.add(content);
        }
    }
}
