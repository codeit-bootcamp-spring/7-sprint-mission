package com.sprint.mission.discodeit.dto.fileIo;

import com.sprint.mission.discodeit.enums.OnlineStatus;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatusIoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userUuid;
    private Instant lastActiveAt;
    private OnlineStatus onlineStatus;

    public UserStatusIoDTO(UUID uuid,
                           Instant createdAt,
                           Instant updatedAt,
                           UUID userUuid,
                           OnlineStatus onlineStatus) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userUuid = userUuid;
        this.onlineStatus = onlineStatus;
    }
}

