package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Message extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    private Instant updateAt;

    private String content;
    private final UUID channelId;
    private final UUID userId;
    private final List<UUID> attachmentIds;

    public Message(String content, UUID channelId, UUID userId, List<UUID> attachmentIds) {
        updateAt = Instant.now();
        this.content = content;
        this.channelId = channelId;
        this.userId = userId;
        this.attachmentIds = attachmentIds;
    }

    public void messageUpdate(String content) {
        boolean isUpdate = false;
        if(this.content != null && !this.content.equals(content)) {
            this.content = content;
            isUpdate = true;
        }

        if(isUpdate) this.updateAt = Instant.now();
    }

}
