package com.sprint.mission.discodeit.exception;

import java.time. Instant;
import java.util. Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String code,
        String message,
        String exceptionType,
        Map<String, Object> details
) {

    public static ErrorResponse of(ErrorCode errorCode, String exceptionType, Map<String, Object> details) {
        return new ErrorResponse(
                Instant. now(),
                errorCode.getStatus().value(),
                errorCode.name(),
                errorCode.getMessage(),
                exceptionType,
                details
        );
    }

    public static ErrorResponse of(int status, String code, String message, String exceptionType, Map<String, Object> details) {
        return new ErrorResponse(
                Instant.now(),
                status,
                code,
                message,
                exceptionType,
                details
        );
    }
}