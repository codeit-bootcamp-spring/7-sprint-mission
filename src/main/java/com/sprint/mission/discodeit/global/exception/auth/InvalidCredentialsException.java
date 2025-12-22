package com.sprint.mission.discodeit.global.exception.auth;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidCredentialsException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
