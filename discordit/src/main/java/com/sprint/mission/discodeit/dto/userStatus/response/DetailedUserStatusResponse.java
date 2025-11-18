package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.time.Instant;
import java.util.UUID;

public record DetailedUserStatusResponse(
        UUID id,
        UUID userId,
        Instant lastActiveAt,
        OnlineStatus onlineStatus
) {
    public static DetailedUserStatusResponse toDto(UserStatus userStatus) {
        return new DetailedUserStatusResponse(
                userStatus.getUuid(),
                userStatus.getUser().getUuid(),
                userStatus.getLastActiveAt(),
                userStatus.getOnlineStatus()
        );
    }
}
