package com.sprint.mission.discodeit.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorInfoRes> handleCustomException(CustomException e, HttpServletRequest request){
        ErrorCode errorCode = e.getErrorCode();
        log.error("[CustomException] {} - {} | url={} | method={} | ip={}",
                errorCode.getCode(),
                errorCode.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                request.getRemoteAddr()
        );
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorInfoRes.of(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfoRes> handleException(CustomException e, HttpServletRequest request){
        log.error("[Exception] {} | url={} | method={} | ip={}",
                e.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                request.getRemoteAddr(),
                e  // stack trace 포함
        );
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorInfoRes.of(errorCode));
    }
}
