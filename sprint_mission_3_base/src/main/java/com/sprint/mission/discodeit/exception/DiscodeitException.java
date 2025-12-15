package com.sprint.mission.discodeit.exception;

import java.util.Map;

public abstract class DiscodeitException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    protected DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
