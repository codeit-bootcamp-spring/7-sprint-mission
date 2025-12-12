package com.sprint.mission.discodeit.global.exception.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UsernameAlreadyExistsException extends UserException {
    public UsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UsernameAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
