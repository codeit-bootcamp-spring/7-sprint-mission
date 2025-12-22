package com.sprint.mission.discodeit.global.exception.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class EmailAlreadyExistsException extends UserException {
    public EmailAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EmailAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
