package com.sprint.mission.discodeit.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorResponse(
        Instant timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,   // 발생한 예외의 클래스 이름
        int status  // HTTP 상태코드
) {
    // 커스텀 예외 처리
    public static ErrorResponse from(DiscodeitException e) {

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .details(e.getDetails())
                .exceptionType(e.getClass().getSimpleName())
                .status(e.getErrorCode().getStatus().value())
                .build();
    }

    // Validation 예외 처리
    public static ErrorResponse of(HttpStatus status, String message,
                                   String exceptionType, Map<String, Object> details) {

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .message(message)
                .details(details)
                .exceptionType(exceptionType)
                .status(status.value())
                .build();
    }

    // 정의되지 않은 예외 처리
    public static ErrorResponse of(HttpStatus status, String message,
                                   String exceptionType, String details) {

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .message(message)
                .details(Map.of("", details))
                .exceptionType(exceptionType)
                .status(status.value())
                .build();
    }
}
