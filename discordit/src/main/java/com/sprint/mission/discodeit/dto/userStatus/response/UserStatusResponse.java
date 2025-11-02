package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        String userId,
        OnlineStatus onlineStatus
) {
    public static UserStatusResponse toDto(UserStatus userStatus) {
        return new UserStatusResponse(
                userStatus.getUuid(),
                userStatus.getUser().getUserId(),
                userStatus.getOnlineStatus()
        );
    }
}
