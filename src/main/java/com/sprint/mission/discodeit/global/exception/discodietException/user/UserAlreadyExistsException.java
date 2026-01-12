package com.sprint.mission.discodeit.global.exception.discodietException.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(String key, Object value) {
        super(ErrorCode.USER_ALREADY_EXISTS, key, value);
    }

    public static UserAlreadyExistsException byUsername(String username) {
        return new UserAlreadyExistsException("username", username);
    }

    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException("email", email);
    }
}
