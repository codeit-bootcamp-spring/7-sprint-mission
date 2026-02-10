package com.sprint.mission.discodeit.global.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public record ErrorResponse (
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,
        int status
) {
    // 실패 응답(커스텀 예외 결과 전달)
    public static ResponseEntity<ErrorResponse> of(DiscodeitException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(
                        e.getTimestamp(),
                        e.getErrorCode().name(),
                        e.getErrorCode().getMessage(),
                        e.getDetails(),
                        e.getClass().getSimpleName(),
                        e.getErrorCode().getStatus().value()
                ));
    }

    // 실패 응답(스프링 기본 예외 결과 전달)
    public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode, Map<String, Object> details, Exception e) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(
                        Instant.now(),
                        errorCode.name(),
                        errorCode.getMessage(),
                        details,
                        e.getClass().getSimpleName(),
                        errorCode.getStatus().value()
                ));
    }

    // 실패 응답(스프링 기본 예외 결과 전달)
    public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode, Exception e) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(
                        Instant.now(),
                        errorCode.name(),
                        errorCode.getMessage(),
                        Collections.emptyMap(),
                        e.getClass().getSimpleName(),
                        errorCode.getStatus().value()
                ));
    }
}
