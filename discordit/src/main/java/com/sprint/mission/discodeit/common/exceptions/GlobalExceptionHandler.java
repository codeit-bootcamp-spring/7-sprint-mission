package com.sprint.mission.discodeit.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.sprint.mission.discodeit.common.exceptions.ErrorResponseMapper.from;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
        log.error("DiscodeitException: {}", ex.getErrorCode(), ex);
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(from(ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error(ex.getClass().getSimpleName(), ex);
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(
                ErrorResponseMapper.from(ex, HttpStatus.BAD_REQUEST, details));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentsExceptions(
            IllegalArgumentException ex) {
        log.error(ex.getClass().getSimpleName(), ex);
        log.error(ex.getMessage(), ex);


        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(from(ex, HttpStatus.BAD_REQUEST, details));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error(ex.getClass().getSimpleName(), ex);
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(from(ex, HttpStatus.INTERNAL_SERVER_ERROR, details));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex) {
        log.error(ex.getClass().getSimpleName(), ex);
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());
        details.put("cause", ex.getCause() != null ? ex.getCause().toString() : null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(from(ex, HttpStatus.INTERNAL_SERVER_ERROR, details));
    }
}
