package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.dto.CustomApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleCustomException(CustomException e) {
    ErrorCode errorCode = e.getErrorCode();
    log.error(e.getMessage());
    CustomApiResponse<Object> response = CustomApiResponse.error(
        errorCode.getStatus(),
        errorCode.getCode(),   // 비즈니스 코드
        errorCode.getMessage() // 사용자 메시지
    );

    return ResponseEntity.status(errorCode.getStatus()).body(response);
  }

  // Validation에 대한 에러 핸들러
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleValidationException(
      MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    log.error(e.getMessage());
    CustomApiResponse<Object> response = CustomApiResponse.error(
        HttpStatus.BAD_REQUEST,
        "VALIDATION_ERROR",
        message
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomApiResponse<Object>> handleException(Exception e) {
    log.error(e.getMessage());
    CustomApiResponse<Object> response = CustomApiResponse.error(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        e.getMessage()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
