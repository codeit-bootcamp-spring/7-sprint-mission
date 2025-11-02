package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorInfoRes> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        log.error("[CustomException] {} - {}", errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorInfoRes.of(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfoRes> handleException(CustomException e){
        log.error("[Exception] {}", e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorInfoRes.of(errorCode));
    }
}
