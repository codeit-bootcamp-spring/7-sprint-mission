package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class LoginPasswordNotMatchException extends UserException{
    public LoginPasswordNotMatchException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
