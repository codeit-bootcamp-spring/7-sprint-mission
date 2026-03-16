package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
