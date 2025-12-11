package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class DiscodeitException extends RuntimeException{

    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.details = details;
    }

    public DiscodeitException(String message, Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(message);
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.details = details;
    }

    public DiscodeitException(String message, Throwable cause, Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(message, cause);
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.details = details;
    }
}
