package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
public class ReadStatus {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;


    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {

        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;

    }


}
