package com.sprint.mission.discodeit.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // 콘솔에도 로그 찍기 (원인 파악용)
        e.printStackTrace();

        // 메시지가 없을 경우 기본 문구로 대체
        String message = (e.getMessage() != null) ? e.getMessage() : "An unexpected server error occurred.";

        // ✅ 단, 실제 예외인 경우에만 에러 응답 반환
        return ResponseEntity.internalServerError().body("[Error] " + message);
    }
}
