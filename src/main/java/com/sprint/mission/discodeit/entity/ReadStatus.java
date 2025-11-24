package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseUpdatableEntity{
    private final UUID userId; // 삭제필요
    private final UUID channelId; // 삭제필요
    private Instant lastReadAt;

    private User user;
    private Channel channel;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant lastReadAt) {
        if(lastReadAt != null && !lastReadAt.equals(this.lastReadAt)){
            this.lastReadAt = lastReadAt;
        }
    }
}
