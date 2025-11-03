package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;        // 어떤 유저가
    private UUID channelId;     // 어떤 채널에서
    private Instant lastReadAt; // 마지막 읽음 시각

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadAt(Instant at) {
        if (at != null && !at.equals(this.lastReadAt)) {
            this.lastReadAt = at;
            this.updatedAt = Instant.now();
        }
    }
    public void update(java.time.Instant newLastReadAt) {
        if (newLastReadAt == null) return;
        this.lastReadAt = newLastReadAt;
        this.updatedAt = java.time.Instant.now();
    }

}
