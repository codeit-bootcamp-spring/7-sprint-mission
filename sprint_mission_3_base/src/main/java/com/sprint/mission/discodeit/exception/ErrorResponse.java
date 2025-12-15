package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String exceptionType,
        Map<String, Object> details
) {
    public static ErrorResponse of(
            ErrorCode errorCode,
            String exceptionType,
            Map<String, Object> details
    ) {
        return new ErrorResponse(
                Instant.now(),
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.getMessage(),
                exceptionType,
                details
        );
    }
}
