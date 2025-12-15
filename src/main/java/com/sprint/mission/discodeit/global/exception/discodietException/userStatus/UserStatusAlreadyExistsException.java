package com.sprint.mission.discodeit.global.exception.discodietException.userStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {
    public UserStatusAlreadyExistsException() {
        super(ErrorCode.USER_STATUS_ALREADY_EXIST);
    }

    public static UserStatusAlreadyExistsException byId(UUID userId) {
        UserStatusAlreadyExistsException userStatusAlreadyExistsException = new UserStatusAlreadyExistsException();
        userStatusAlreadyExistsException.updateDetail("userId", userId);
        return userStatusAlreadyExistsException;
    }
}
