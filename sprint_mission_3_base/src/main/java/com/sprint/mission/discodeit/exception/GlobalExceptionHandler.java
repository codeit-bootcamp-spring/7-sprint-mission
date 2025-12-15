package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외
    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        log.warn("DiscodeitException 발생", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(
                errorCode,
                e.getClass().getSimpleName(),
                e.getDetails()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    // Validation 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation 실패", e);

        Map<String, Object> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        ErrorResponse response = new ErrorResponse(
                java.time.Instant.now(),
                400,
                "Bad Request",
                "요청 값 검증에 실패했습니다.",
                e.getClass().getSimpleName(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    // 그 외 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("처리되지 않은 예외", e);

        ErrorResponse response = ErrorResponse.of(
                ErrorCode.INTERNAL_ERROR,
                e.getClass().getSimpleName(),
                null
        );

        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getStatus())
                .body(response);
    }
}
