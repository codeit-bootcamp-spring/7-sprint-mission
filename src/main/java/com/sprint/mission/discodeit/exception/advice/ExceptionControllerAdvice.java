package com.sprint.mission.discodeit.exception.advice;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.service.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {


    @ExceptionHandler(DiscodeitException.class)
    public ErrorResponse discodeitException(DiscodeitException e) {
        log.info("[ExceptionHandler] {}", e.getErrorCode());
        return new ErrorResponse(
                e.getTimestamp(),
                e.getErrorCode().toString(),
                e.getErrorCode().getMessage(),
                e.getDetails(),
                "UserNotFoundException",
                400);
    }

    @ExceptionHandler(BindException.class)
    public ErrorResponse bindException(BindException e) {
        log.info("[ExceptionHandler] {}", e.getMessage());
        return new ErrorResponse(
                Instant.now(),
                null,
                e.getMessage(),
                null,
                "BindException",
                400);
    }


}
