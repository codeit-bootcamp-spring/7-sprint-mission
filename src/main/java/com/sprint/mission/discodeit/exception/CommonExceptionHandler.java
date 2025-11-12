package com.sprint.mission.discodeit.exception;


import com.sprint.mission.discodeit.entity.common.Common;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgsHandler(IllegalArgumentException e){
        e.printStackTrace();



        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidHandler(MethodArgumentNotValidException e){
        e.printStackTrace();

        //가공이 필요하다
        Map<String,String> errors= new HashMap<>();

        BindingResult bindingResult = e.getBindingResult();


        e.getBindingResult().getFieldErrors().forEach(error->{
                String field = error.getField();
             String message = error.getDefaultMessage();
        errors.put(field, message);
        });

        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }



    //모르는 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
