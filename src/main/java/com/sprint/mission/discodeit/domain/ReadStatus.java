package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
public class ReadStatus {

    private String id;
    private Instant createdAt;
    private Instant updatedAt;
    private String userId;
    private String channelId;
    private Instant lastReadAt;


    public ReadStatus(String userId, String channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;

    }

    public void read(){
        this.lastReadAt=Instant.now();
    }

    public Long timeSinceLastRead() {
        if (lastReadAt == null) {
            return null;
        }
        return Duration.between(lastReadAt, Instant.now()).toMinutes();
    }
}
