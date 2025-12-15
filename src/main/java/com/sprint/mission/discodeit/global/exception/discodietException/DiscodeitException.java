package com.sprint.mission.discodeit.global.exception.discodietException;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DiscodeitException extends RuntimeException {

    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.details = new HashMap<>();
    }

    public DiscodeitException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.details = new HashMap<>();
    }

    public void updateDetail(String key, Object value) {
        this.details.put(key, value);
    }
}
