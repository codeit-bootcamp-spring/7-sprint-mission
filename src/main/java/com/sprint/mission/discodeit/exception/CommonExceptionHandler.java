package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.domain.DiscodeitException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> discodeitExceptionHandler(DiscodeitException e){
        ErrorResponse errorResponse = new ErrorResponse(
                e.getTimestamp(),
                e.getErrorCode().toString(),
                e.getMessage(),
                e.getDetails(),
                e.getClass().getName(),
                400
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        Map<String,Object> details = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                fieldError -> details.put(fieldError.getField(),fieldError.getDefaultMessage())
        );
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                ErrorCode.VALID_FAIL.name(),
                ErrorCode.VALID_FAIL.getMessage(),
                details,
                e.getClass().getName(),
                400
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(UserNotExistException.class)
//    public ResponseEntity<ErrorResponse> userNotFoundExceptionHandler(UserNotExistException e){
//        ErrorResponse errorResponse = new ErrorResponse(
//                e.getTimestamp(),
//                e.getErrorCode().toString(),
//                e.getMessage(),
//                e.getDetails(),
//                e.getClass().getName(),
//                400
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> reallyRestExceptionHandler(Exception e){
       log.error("reallyRestExceptionHandler : {}",e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                null,
                e.getClass().getName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
