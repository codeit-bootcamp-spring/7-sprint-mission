package com.sprint.mission.discodeit.common.exceptions;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
        log.error("DiscodeitException: {}", ex.getErrorCode(), ex);

        ErrorResponse errorResponse = ex.toErrorResponse();
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.INVALID_ARGS.name())
                .message(ErrorCode.INVALID_ARGS.getDescription())
                .details(errors)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentsExceptions(
            IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.ILLEGAL_ARGUMENT.name())
                .message(ErrorCode.ILLEGAL_ARGUMENT.getDescription())
                .details(details)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .message("NPE가 발생했습니다.")
                .details(details)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());
        details.put("cause", ex.getCause() != null ? ex.getCause().toString() : null);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getDescription())
                .details(details)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
