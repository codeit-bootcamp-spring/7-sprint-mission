package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.UUID;

public record UserFindResponse(
        UUID userId,
        String username,
        UserStatus userStatus
) {
    public static UserFindResponse from(User user, UserStatus status) {
        return new UserFindResponse(
                user.getId(),
                user.getUsername(),
                status
        );
    }
}
