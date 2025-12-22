package com.sprint.mission.discodeit.global.exception.auth;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidLoginRequestException extends AuthException {
    public InvalidLoginRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidLoginRequestException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
