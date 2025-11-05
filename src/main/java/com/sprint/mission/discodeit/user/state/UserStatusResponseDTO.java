package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.config.enums.Status;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDTO(
        UUID id,
        Status currentStatus,
        String message,
        Instant lastOnlineAt
) {
    public static UserStatusResponseDTO fromEntity(UserStatus UserStatus) {
        return new UserStatusResponseDTO(
                UserStatus.getId(),
                UserStatus.getCurrentStatus(),
                UserStatus.getCustomStatusMessage(),
                UserStatus.getLastOnlineAt()
        );
    }
}
