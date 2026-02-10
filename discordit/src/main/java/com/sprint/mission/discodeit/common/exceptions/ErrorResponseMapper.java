package com.sprint.mission.discodeit.common.exceptions;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

public class ErrorResponseMapper {

    public static ErrorResponse from(DiscodeitException ex) {
        return ErrorResponse.builder()
                .timestamp(ex.getTimestamp())
                .code(ex.getErrorCode().name())
                .message(ex.getErrorCode().getDescription())
                .details(ex.getDetails())
                .exceptionType(ex.getClass().getSimpleName())
                .status(ex.getErrorCode().getHttpStatus().value())
                .build();
    }

    public static <T extends Exception> ErrorResponse from(T ex, HttpStatus status) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(status.name())
                .message(ex.getMessage())
                .exceptionType(ex.getClass().getSimpleName())
                .status(status.value())
                .build();
    }

    public static <T extends Exception> ErrorResponse from(T ex, HttpStatus status, Map<String, Object> details) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(status.name())
                .message(ex.getMessage())
                .exceptionType(ex.getClass().getSimpleName())
                .status(status.value())
                .details(details)
                .build();
    }
}
