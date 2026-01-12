package com.sprint.mission.discodeit.global.exception.discodietException.auth;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class InvalidCredentialsException extends AuthException {

    public InvalidCredentialsException(String key, Object value) {
        super(ErrorCode.INVALID_CREDENTIALS, key, value);
    }

    public static InvalidCredentialsException byUsername(String username) {
        return new InvalidCredentialsException("username", username);
    }

    public static InvalidCredentialsException byPassword(String password) {
        return new InvalidCredentialsException("password", password);
    }
}
