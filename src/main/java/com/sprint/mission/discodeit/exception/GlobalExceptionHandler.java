package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> discodeitException(DiscodeitException e) {
    ErrorResponse errorResponse = ErrorResponse.from(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
    ErrorResponse errorResponse = ErrorResponse.of(e, "BAD_REQUEST", HttpStatus.BAD_REQUEST);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> illegalStateException(IllegalStateException e) {
    ErrorResponse errorResponse = ErrorResponse.of(e, "BAD_REQUEST", HttpStatus.BAD_REQUEST);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    ErrorResponse errorResponse = ErrorResponse.of(e, "METHOD_NOT_ALLOWED",
        HttpStatus.METHOD_NOT_ALLOWED);
    return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    ErrorResponse errorResponse = ErrorResponse.of(e, "BAD_REQUEST", HttpStatus.BAD_REQUEST);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorResponse> nullPointerException(NullPointerException e) {
    ErrorResponse errorResponse = ErrorResponse.of(e, "BAD_REQUEST", HttpStatus.BAD_REQUEST);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exception(Exception e) {
    ErrorResponse errorResponse = ErrorResponse.of(e, "INTERNAL_SERVER_ERROR",
        HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
  }
}
