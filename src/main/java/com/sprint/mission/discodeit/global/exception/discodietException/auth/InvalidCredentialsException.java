package com.sprint.mission.discodeit.global.exception.discodietException.auth;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserAlreadyExistsException;

public class InvalidCredentialsException extends AuthException {

    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }

    public static InvalidCredentialsException byUsername(String username) {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException();
        userAlreadyExistsException.updateDetail("username", username);
        return new InvalidCredentialsException();
    }

    public static InvalidCredentialsException byPassword(String password) {
        UserAlreadyExistsException userAlreadyExistsException = new UserAlreadyExistsException();
        userAlreadyExistsException.updateDetail("password", password);
        return new InvalidCredentialsException();
    }
}
