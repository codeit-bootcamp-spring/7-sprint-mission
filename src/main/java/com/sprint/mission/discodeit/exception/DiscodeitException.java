package com.sprint. mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;

public class DiscodeitException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode) {
        super(errorCode. getMessage());
        this.errorCode = errorCode;
        this. details = new HashMap<>();
    }

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details != null ? details : new HashMap<>();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public DiscodeitException addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }
}