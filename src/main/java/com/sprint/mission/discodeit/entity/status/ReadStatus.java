package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseUpdateEntity {

    private static final long serialVersionUID = 1L;

    //
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    //추가
    User user;


    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        //
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant newLastReadAt) {
        boolean anyValueUpdated = false;
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
            anyValueUpdated = true;
        }

    }
}

