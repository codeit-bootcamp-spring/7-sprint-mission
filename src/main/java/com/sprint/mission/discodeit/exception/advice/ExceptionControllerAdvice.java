package com.sprint.mission.discodeit.exception.advice;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.service.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {


    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> discodeitException(DiscodeitException e) {
        log.info("[ExceptionHandler] {}", e.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse(
                e.getTimestamp(),
                e.getErrorCode().toString(),
                e.getErrorCode().getMessage(),
                e.getDetails(),
                "DiscodeitException",
                400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        log.info("[ExceptionHandler] {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                null,
                e.getMessage(),
                null,
                "BindException",
                400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> notificationNotFoundException(NotificationNotFoundException e) {
        log.info("[ExceptionHandler] {}", e.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse(
                e.getTimestamp(),
                e.getErrorCode().toString(),
                e.getErrorCode().getMessage(),
                e.getDetails(),
                "NotificationNotFoundException",
                404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


}
