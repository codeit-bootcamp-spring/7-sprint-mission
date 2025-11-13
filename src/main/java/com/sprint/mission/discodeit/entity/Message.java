package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Message extends BaseEntity {
    private String content;
    private final UUID channelId;
    private final UUID authorId;
    private final List<UUID> attachmentIds;

    @Builder
    public Message(UUID authorId, UUID channelId, String content, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds != null ? attachmentIds : new ArrayList<>();
    }

    public void updateContent(String content) {
        this.content = content;
        updateTimestamp();
    }
}



