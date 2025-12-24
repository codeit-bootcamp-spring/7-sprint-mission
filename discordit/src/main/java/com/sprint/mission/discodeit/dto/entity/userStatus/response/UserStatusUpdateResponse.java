package com.sprint.mission.discodeit.dto.entity.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;

public record UserStatusUpdateResponse(
        Instant newLastActiveAt
) {
    public static UserStatusUpdateResponse toDto(UserStatus userStatus) {
        return new UserStatusUpdateResponse(
                userStatus.getLastActiveAt()
        );
    }
}
