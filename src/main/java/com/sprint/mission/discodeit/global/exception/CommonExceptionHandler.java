package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(DiscodeitException e){
        log.warn(buildLogMessage(e));
        return ErrorResponse.error(e);
    }

    private String buildLogMessage(DiscodeitException e) {
        StringBuilder sb = new StringBuilder();

        sb.append("[")
                .append(e.getErrorCode().name())
                .append("] ")
                .append(e.getErrorCode().getMessage());

        if(e.getDetails() != null && !e.getDetails().isEmpty()){
            sb.append(" ");
            e.getDetails().forEach(
                    (key, value) ->
                            sb.append(key).append("=").append(value).append(" "));
        }

        sb.append("timestamp=").append(e.getTimestamp());

        return sb.toString().trim();
    }

    // 준비 되지 않은 예외 발생시 처리할 메서드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        ErrorCode errorCode = ErrorCode.UNHANDLED_EXCEPTION;

        log.error("[Exception] 예기치 못한 오류 발생: {}", e.getMessage());
        return ErrorResponse.error(errorCode, e);
    }
}
