package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(DiscodeitException e) {
        log.error("Error: {}, details: {}", e.getErrorCode(), e.getDetails());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.from(e));
    }

    // Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        Map<String, Object> details = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> Objects.requireNonNullElse(
                                fe.getDefaultMessage(),
                                "알려지지 않은 에러"
                        ),
                        (existingMessage, newMessage) -> existingMessage
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        "입력값이 유효하지 않습니다.",
                        e.getClass().getSimpleName(),
                        details
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND,
                        "리소스를 찾지 못했습니다.",
                        e.getClass().getSimpleName(),
                        e.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("No Handled Error: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류입니다.",
                        e.getClass().getSimpleName(),
                        e.getMessage()
                ));
    }
}
