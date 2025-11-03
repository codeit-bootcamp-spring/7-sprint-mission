package com.sprint.mission.discodeit.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // @RestController 있어야 동작!!
public class CommonExceptionHandler { //?? 안돼??? 왱????

    // ❤️ Controller 단에서 발생하는 모든 예외를 일괄 처리하는 클래스
    // ❤️ 실제 예외는 Service 계층에서 대부분 발생하지만, 따로 예외 처리가 없는 경우
    // ❤️ 메서드를 호출한 상위 계층으로 전파됩니다.
    // ❤️ book, user, rental controller들 여기에서 다 처리

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgsHandler(IllegalArgumentException e){
        e.printStackTrace();
        // 예외의 원인을 http 상태 코드와 메세지를 통해서 알려주고 싶다 -> ResponseEntity
        PrintUtil.errMessage("💙💙💙💙💙");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}