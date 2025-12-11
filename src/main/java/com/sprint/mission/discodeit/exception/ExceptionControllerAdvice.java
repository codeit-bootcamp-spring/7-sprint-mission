package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {



    @ExceptionHandler
    public ResponseEntity<String> illegalException(IllegalArgumentException e){
        log.info("[ExceptionHandler] {}",e.getMessage());
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String allException(Exception e){
//        log.info("[ExceptionHandler] {}",e.getMessage());
//        return e.getMessage();
//    }

    @ExceptionHandler
    public String lazyInitializationException(LazyInitializationException e){
        log.info("[ExceptionHandler] {}",e.getMessage());
        return e.getMessage();
    }

}
