package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseMapper errorResponseMapper;

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> DiscodeitException(DiscodeitException e) {
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(e);
        return ResponseEntity.status(errorResponse.status()).body(errorResponse);
    }

    // Validation에 대한 에러 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> details = new HashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(error ->
                        details.put(error.getField(), error.getDefaultMessage())
                );

        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(e, ErrorCode.VALIDATION_ERROR, details);
        return ResponseEntity.status(errorResponse.status()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        Map<String, Object> details = new HashMap<>();
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(e, ErrorCode.VALIDATION_ERROR, details);
        return ResponseEntity.status(errorResponse.status()).body(errorResponse);
    }
}
