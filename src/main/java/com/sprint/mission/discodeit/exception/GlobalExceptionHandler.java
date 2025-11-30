package com.sprint.mission.discodeit.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorInfoRes> handleValidationException(
      MethodArgumentNotValidException e, HttpServletRequest request) {

    FieldError fieldError = e.getBindingResult().getFieldError(); // 첫 번째 오류 처리
    ErrorCode errorCode;

    switch (fieldError.getField()) {
      case "email":
        errorCode = ErrorCode.EMAIL_INVALID;
        break;
      case "nickname":
        errorCode = ErrorCode.NICKNAME_INVALID;
        break;
      case "password":
        errorCode = ErrorCode.PASSWORD_INVALID;
        break;
      default:
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        break;
    }

    log.error("[ValidationError] {} - field={} value={} | url={} | method={} | ip={}",
        errorCode.getCode(),
        fieldError.getField(),
        fieldError.getRejectedValue(),
        request.getRequestURI(),
        request.getMethod(),
        request.getRemoteAddr()
    );

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorInfoRes.from(errorCode));
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorInfoRes> handleCustomException(CustomException e,
      HttpServletRequest request) {
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
        .body(ErrorInfoRes.from(errorCode));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorInfoRes> handleException(Exception e,
      HttpServletRequest request) {
    log.error("[Exception] {} | url={} | method={} | ip={}",
        e.getMessage(),
        request.getRequestURI(),
        request.getMethod(),
        request.getRemoteAddr()
        //e  // stack trace 포함
    );
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorInfoRes.from(errorCode));
  }
}
