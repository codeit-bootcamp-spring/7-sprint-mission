package com.sprint.mission.discodeit.global.exception.discodietException.userStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
    public UserStatusNotFoundException(String key, Object value) {
        super(ErrorCode.USER_STATUS_NOT_FOUND, key, value);
    }

    public static UserStatusNotFoundException byUserId(UUID userId) {
        UserStatusNotFoundException userStatusNotFoundException = new UserStatusNotFoundException("userId", userId);
        return userStatusNotFoundException;
    }

    public static UserStatusNotFoundException byId(UUID userStatusId) {
        UserStatusNotFoundException userStatusNotFoundException = new UserStatusNotFoundException("userStatusId", userStatusId);
        return userStatusNotFoundException;
    }
}
