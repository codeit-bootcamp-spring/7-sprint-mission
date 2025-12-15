package com.sprint.mission.discodeit.global.exception.discodietException.userStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
    public UserStatusNotFoundException() {
        super(ErrorCode.USER_STATUS_NOT_FOUND);
    }

    public static UserStatusNotFoundException byId(UUID userStatusId) {
        UserStatusNotFoundException userStatusNotFoundException = new UserStatusNotFoundException();
        userStatusNotFoundException.updateDetail("userStatusId", userStatusId);
        return userStatusNotFoundException;
    }
}
