package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorCodeStatusMapper statusMapper;

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeit(DiscodeitException e) {
        HttpStatus status = statusMapper.map(e.getErrorCode());
        return ResponseEntity.status(status).body(new ErrorResponse(
                e.getTimestamp().toString(),
                e.getErrorCode().name(),
                e.getErrorCode().getMessage(),
                e.getDetails(),
                e.getClass().getSimpleName(),
                status.value()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, Object> details = new LinkedHashMap<>();
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        details.put("fieldErrors", fieldErrors);

        HttpStatus status = statusMapper.map(ErrorCode.VALIDATION_FAILED);
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now().toString(),
                ErrorCode.VALIDATION_FAILED.name(),
                ErrorCode.VALIDATION_FAILED.getMessage(),
                details,
                e.getClass().getSimpleName(),
                status.value()
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(HttpMessageNotReadableException e) {
        HttpStatus status = statusMapper.map(ErrorCode.BAD_REQUEST);
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now().toString(),
                ErrorCode.BAD_REQUEST.name(),
                "Request body is invalid or unreadable.",
                Map.of(),
                e.getClass().getSimpleName(),
                status.value()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        HttpStatus status = statusMapper.map(ErrorCode.INTERNAL_ERROR);
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now().toString(),
                ErrorCode.INTERNAL_ERROR.name(),
                ErrorCode.INTERNAL_ERROR.getMessage(),
                Map.of(),
                e.getClass().getSimpleName(),
                status.value()
        ));
    }
}
