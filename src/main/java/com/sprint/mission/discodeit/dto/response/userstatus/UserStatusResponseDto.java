package com.sprint.mission.discodeit.dto.response.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
        UUID id,
        UUID userId,
        Instant lastActiveAt,
        boolean online,
        Instant createdAt,
        Instant updatedAt) {

    public static UserStatusResponseDto from(UserStatus userStatus) {
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastReadAt(),
                userStatus.isOnlineNow(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt()
        );
    }
}
