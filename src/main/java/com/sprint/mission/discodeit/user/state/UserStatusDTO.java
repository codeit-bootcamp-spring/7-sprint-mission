package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.config.enums.Status;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDTO(
        UUID id,
        Status currentStatus,
        String message,
        Instant lastOnlineAt
) {
    public static UserStatusDTO fromEntity(UserStatus UserStatus) {
        return new UserStatusDTO(
                UserStatus.getId(),
                UserStatus.getCurrentStatus(),
                UserStatus.getCustomStatusMessage(),
                UserStatus.getLastOnlineAt()
        );
    }
}
