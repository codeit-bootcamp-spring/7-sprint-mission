package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity{
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant lastReadAt) {
        boolean flag = false;

        if(lastReadAt != null && lastReadAt.equals(this.lastReadAt)){
            this.lastReadAt = lastReadAt;
            flag = true;
        }
        if(flag){
            this.setUpdatedAt();
        }
    }
}
