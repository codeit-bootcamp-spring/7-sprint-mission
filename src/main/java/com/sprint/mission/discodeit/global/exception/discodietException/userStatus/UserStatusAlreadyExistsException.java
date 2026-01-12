package com.sprint.mission.discodeit.global.exception.discodietException.userStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {
    public UserStatusAlreadyExistsException(String key, Object value) {
        super(ErrorCode.USER_STATUS_ALREADY_EXIST, key, value);
    }

    public static UserStatusAlreadyExistsException byId(UUID userId) {
        return new UserStatusAlreadyExistsException("userId", userId);
    }
}
