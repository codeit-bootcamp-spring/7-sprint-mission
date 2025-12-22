package com.sprint.mission.discodeit.global.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Getter
public abstract class DiscodeitException extends RuntimeException {
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode) {
        timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = Collections.emptyMap();
    }

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }


}
