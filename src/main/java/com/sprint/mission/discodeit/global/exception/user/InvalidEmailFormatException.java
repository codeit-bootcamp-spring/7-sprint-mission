package com.sprint.mission.discodeit.global.exception.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidEmailFormatException extends UserException {
    public InvalidEmailFormatException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidEmailFormatException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
