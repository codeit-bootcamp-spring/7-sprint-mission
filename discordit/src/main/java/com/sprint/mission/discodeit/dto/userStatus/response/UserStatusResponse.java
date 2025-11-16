package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
        Instant newLastActiveAt
) {
    public static UserStatusResponse toDto(UserStatus userStatus) {
        return new UserStatusResponse(
                userStatus.getLastActiveAt()

        );
    }
}
