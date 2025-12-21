package com.sprint.mission.discodeit.common.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,
        int status
) {
    public static ErrorResponse from(
            Instant timestamp,
            ErrorCode errorCode,
            String message,
            Map<String, Object> details,
            String exceptionType,
            int status
    ) {
        return new ErrorResponse(
                timestamp,
                errorCode == null ? null : errorCode.name(),
                message,
                details == null ? Map.of() : details,
                exceptionType,
                status
        );
    }
}
