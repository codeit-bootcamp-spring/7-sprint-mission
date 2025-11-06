package com.sprint.mission.discodeit.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgsHandler(IllegalArgumentException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
