package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(DiscodeitException e){
        log.warn(buildLogMessage(e));
        return ErrorResponse.of(e);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String summary = e.getBindingResult().getFieldErrors().stream()
                .map(err -> "%s='%s' (%s)".formatted(
                        err.getField(),
                        err.getRejectedValue(),
                        err.getDefaultMessage()
                ))
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", summary);

        Map<String, Object> details = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage())
        );

        return ErrorResponse.of(ErrorCode.VALIDATION_ERROR, details, e);
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(Exception e, HttpServletRequest request) {
        String customMessage = "권한이 부족합니다.";

        if(request.getRequestURI().contains("/channels")) {
            customMessage = "채널 관리자(CHANNEL_MANAGER) 권한이 필요합니다.";
        } else if(request.getRequestURI().contains("/auth/role")) {
            customMessage = "관리자(ADMIN) 권한이 필요합니다.";
        } else if(request.getRequestURI().contains("/api/notifications")) {
            customMessage = "요청자 본인의 알림에 대해서만 요청이 가능합니다.";
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(
                        Instant.now(),
                        "ACCESS_DENIED",
                        customMessage,
                        Collections.emptyMap(),
                        e.getClass().getSimpleName(),
                        403
                ));
    }

    // 준비 되지 않은 예외 발생시 처리할 메서드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        log.error("[Exception] 예기치 못한 오류 발생: {}", e.getMessage());
        return ErrorResponse.of(ErrorCode.UNHANDLED_EXCEPTION, e);
    }
}
