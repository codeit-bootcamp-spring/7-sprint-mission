package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class UserAlreadyExistsException extends UserException{
    public UserAlreadyExistsException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }

    public UserAlreadyExistsException(String message, Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(message, timestamp, errorCode, details);
    }

    public UserAlreadyExistsException(String message, Throwable cause, Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(message, cause, timestamp, errorCode, details);
    }
}
