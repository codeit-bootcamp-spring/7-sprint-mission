package com.sprint.mission.discodeit.global.exception.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidPasswordFormatException extends UserException {
    public InvalidPasswordFormatException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidPasswordFormatException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
