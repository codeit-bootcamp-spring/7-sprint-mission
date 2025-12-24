package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@ResponseBody
@RestControllerAdvice
public class ErrorExceptionHandler {

    // @Valid 유효성 검사 실패시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
            Instant.now(),
            e.getStatusCode().toString(),
            e.getMessage(),
            e.getBody().getProperties(),
            MethodArgumentNotValidException.class.toString(),
            400
        );

        return ResponseEntity
            .status(response.status)
            .body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
            e,
            UserNotFoundException.class.toString(),
            404
        );

        return ResponseEntity
            .status(response.status)
            .body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
            e,
            UserAlreadyExistsException.class.toString(),
            409
        );

        return ResponseEntity
            .status(response.status)
            .body(response);
    }

    @ExceptionHandler(channelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChannelNotFoundException(channelNotFoundException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
            e,
            channelNotFoundException.class.toString(),
            404
        );

        return ResponseEntity
            .status(response.status)
            .body(response);
    }


    @ExceptionHandler(PrivateChannelUpdateException.class)
    public ResponseEntity<ErrorResponse> handlePrivateChannelUpdateException(PrivateChannelUpdateException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
            e,
            PrivateChannelUpdateException.class.toString(),
            400
        );

        return ResponseEntity
            .status(response.status)
            .body(response);
    }

    @ExceptionHandler(UserStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserStatusNotFoundException(UserStatusNotFoundException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
            e,
            UserStatusNotFoundException.class.toString(),
            404
        );

        return ResponseEntity
            .status(response.status)
            .body(response);
    }

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
            e,
            DiscodeitException.class.toString(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity
            .status(response.status)
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}