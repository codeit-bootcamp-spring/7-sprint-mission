package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.UUID;

public record UserStatusResponse(
        UUID UserStatusId
) {
    public static UserStatusResponse from(UserStatus userStatus) {
        return new UserStatusResponse(
                userStatus.getId()

        );
    }
}
