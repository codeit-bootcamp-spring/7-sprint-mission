package com.sprint.mission.discodeit.global.exception.discodietException.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }

    public static UserAlreadyExistsException byUsername(String username) {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException();
        userAlreadyExistsException.updateDetail("username", username);
        return userAlreadyExistsException;
    }

    public static UserAlreadyExistsException byEmail(String email) {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException();
        userAlreadyExistsException.updateDetail("email", email);
        return userAlreadyExistsException;
    }
}
