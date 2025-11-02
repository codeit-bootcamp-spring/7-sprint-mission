package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReadStatus extends Common{
    @Serial
    private static final long serialVersionUID = 1L;
    private Instant updateAt;

    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.updateAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant lastReadAt) {
        boolean isUpdate = false;
        if(lastReadAt != null && !this.lastReadAt.equals(lastReadAt)) {
            this.lastReadAt = lastReadAt;
            isUpdate = true;
        }

        if(isUpdate) this.updateAt = Instant.now();
    }
}
