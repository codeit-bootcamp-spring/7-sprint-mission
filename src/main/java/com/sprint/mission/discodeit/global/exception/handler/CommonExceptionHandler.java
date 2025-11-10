package com.sprint.mission.discodeit.global.exception.handler;

import com.sprint.mission.discodeit.global.dto.ApiResponse;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> customExceptionHandler(CustomException e) {
        log.error("[handleCustomException] {} : {}",e.getErrorCode().name(), e.getErrorCode().getMessage());
        return ApiResponse.error(e);
    }

    // 준비 되지 않은 예외 발생시 처리할 메서드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> exceptionHandler(Exception e) {
        log.error("[Exception] 예기치 못한 오류 발생: {}", e.getMessage());
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,"예기치 못한 오류가 발생했습니다.");
    }
}
