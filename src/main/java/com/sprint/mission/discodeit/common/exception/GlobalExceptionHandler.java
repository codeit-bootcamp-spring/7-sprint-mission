package com.sprint.mission.discodeit.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        ErrorCode code = e.getErrorCode() != null ? e.getErrorCode() : ErrorCode.INTERNAL_ERROR;
        HttpStatus status = code.getStatus();
        String message = code.getMessage();
//        HttpStatus status = mapStatus(e.getErrorCode());

        ErrorResponse body = ErrorResponse.from(
                e.getTimestamp(),
                e.getErrorCode(),
                message,
                e.getDetails(),
                e.getClass().getSimpleName(),
                status.value()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ErrorCode code = ErrorCode.INVALID_REQUEST;
        HttpStatus status = code.getStatus();

        Map<String, Object> details = new LinkedHashMap<>();
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            // 동일 필드에 에러가 여러개면 첫 번째만 유지
            fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        details.put("fieldErrors", fieldErrors);

        ErrorResponse body = ErrorResponse.from(
                Instant.now(),
                code,
                code.getMessage(),
                details,
                exception.getClass().getSimpleName(),
                status.value()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = code.getStatus();

        ErrorResponse body = ErrorResponse.from(
                Instant.now(),
                code,
                code.getMessage(),
                Map.of(),
                exception.getClass().getSimpleName(),
                status.value()
        );

        return ResponseEntity.status(status).body(body);
    }
}
