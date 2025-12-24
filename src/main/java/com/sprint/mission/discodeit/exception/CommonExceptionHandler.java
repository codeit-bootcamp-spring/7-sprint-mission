//package com.sprint.mission.discodeit.common;
//
//import java.util.NoSuchElementException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice // @RestController 있어야 동작!!
//@ResponseBody
//@Slf4j
//public class CommonExceptionHandler {
//
//    // ❤️ Controller 단에서 발생하는 모든 예외를 일괄 처리하는 클래스
//    // ❤️ 실제 예외는 Service 계층에서 대부분 발생하지만, 따로 예외 처리가 없는 경우
//    // ❤️ 메서드를 호출한 상위 계층으로 전파됩니다.
//    // ❤️ book, user, rental controller들 여기에서 다 처리
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<?> illegalArgsHandler(IllegalArgumentException e) {
//        log.error(e.getMessage(), e);
////        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//
//        // 예외의 원인을 http 상태 코드와 메세지를 통해 알려주고 싶다. -> ResponseEntity
//        ApiResponse<Object> response = ApiResponse.error("ILLEGAL_ARGS", e.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<?> noSuchElementHandler(NoSuchElementException e) {
//        log.error(e.getMessage(), e);
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
//    }
//
//
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<?> illegalStateHandler(IllegalStateException e) {
//        log.error(e.getMessage(), e);
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> methodArgsNotValidHandler(MethodArgumentNotValidException e) {
//        log.error(e.getMessage(), e);
//
//        // 1. 오류 결과를 담을 Map을 생성합니다. (Key: 필드명, Value: 에러 메세지)
//        Map<String, String> errors = new HashMap<>();
//
//        /*
//        // BindingResult: 오류 결과 보고서
//        BindingResult bindingResult = e.getBindingResult();
//
//        // BindingResult에서 @Valid에 실패한 필드 목록을 불러옵니다.
//        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//        for (FieldError error : fieldErrors) {
//            String field = error.getField();
//            String content = error.getDefaultMessage();
//            errors.put(field, content);
//        }
//        */
//        e.getBindingResult().getFieldErrors().forEach(error -> {
//            String field = error.getField();
//            String message = error.getDefaultMessage();
//            errors.put(field, message);
//        });
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(errors);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> exceptionHandler(Exception e) {
//        log.error(e.getMessage(), e);
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}