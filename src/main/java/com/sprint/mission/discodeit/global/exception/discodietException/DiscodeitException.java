package com.sprint.mission.discodeit.global.exception.discodietException;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class DiscodeitException extends RuntimeException {

    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode, String key, Object value) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.details = Map.of(key, value);
    }

    public DiscodeitException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.details = Map.of(key, value);
    }

}
