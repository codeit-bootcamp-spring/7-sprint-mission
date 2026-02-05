package com.sprint.mission.discodeit.common.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = HttpStatus.CONFLICT;

        Throwable root = NestedExceptionUtils.getMostSpecificCause(e);

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("rootCause", root == null ? null : root.getClass().getSimpleName());
        details.put("rootMessage", root == null ? null : root.getMessage());

        ErrorResponse body = ErrorResponse.from(
                Instant.now(),
                code,
                "데이터 무결성 제약조건 위반",
                details,
                e.getClass().getSimpleName(),
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        ErrorCode code = ErrorCode.INVALID_REQUEST;
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
