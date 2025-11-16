package com.sprint.mission.discodeit.common.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // TODO: ApiStatus를 이용해 더 구체적인 에러 띄우기

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        log.error(ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentsExceptions(
            IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "BadRequest");
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", "서버 내부 오류가 발생했습니다.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String detailedMessage = "요청 본문을 읽을 수 없습니다.";
        String fieldInfo = "";

        Throwable cause = ex.getCause();

        log.error("=== JSON 파싱 오류 상세 정보 ===");
        log.error("예외 타입: {}", cause != null ? cause.getClass().getName() : "null");
        log.error("전체 스택 트레이스:", ex);

        // Jackson 파싱 오류 상세 정보 추출
        if (cause instanceof MismatchedInputException) {
            MismatchedInputException mismatchEx = (MismatchedInputException) cause;

            log.error("Path 정보: {}", mismatchEx.getPath());
            log.error("Path size: {}", mismatchEx.getPath().size());

            String fieldPath = mismatchEx.getPath().stream()
                    .map(ref -> {
                        log.error("Reference - fieldName: {}, index: {}, from: {}",
                                ref.getFieldName(), ref.getIndex(), ref.getFrom());
                        return ref.getFieldName();
                    })
                    .filter(name -> name != null && !name.isEmpty())
                    .collect(Collectors.joining("."));

            Class<?> targetType = mismatchEx.getTargetType();

            if (fieldPath.isEmpty()) {
                fieldPath = "알 수 없는 필드";
                // Path에서 정보를 못 가져온 경우 메시지에서 추출 시도
                String message = ex.getMessage();
                if (message.contains("through reference chain")) {
                    int startIdx = message.indexOf("reference chain: ") + 17;
                    int endIdx = message.indexOf("]", startIdx);
                    if (startIdx > 17 && endIdx > startIdx) {
                        fieldPath = message.substring(startIdx, endIdx + 1);
                    }
                }
            }

            fieldInfo = String.format("필드: %s, 예상 타입: %s", fieldPath, targetType.getSimpleName());
            detailedMessage = String.format("'%s' 필드의 형식이 올바르지 않습니다. %s 타입이어야 합니다.",
                    fieldPath, targetType.getSimpleName());

            log.error("파싱된 정보 - {}", fieldInfo);
        } else if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidEx = (InvalidFormatException) cause;
            String fieldPath = invalidEx.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(name -> name != null && !name.isEmpty())
                    .collect(Collectors.joining("."));

            if (fieldPath.isEmpty()) {
                fieldPath = "알 수 없는 필드";
            }

            fieldInfo = String.format("필드: %s, 입력값: %s, 예상 타입: %s",
                    fieldPath, invalidEx.getValue(), invalidEx.getTargetType().getSimpleName());
            detailedMessage = String.format("'%s' 필드의 값 '%s'이(가) 올바르지 않습니다.",
                    fieldPath, invalidEx.getValue());

            log.error("파싱된 정보 - {}", fieldInfo);
        } else {
            log.error("알 수 없는 파싱 오류 | 원본 메시지: {}", ex.getMessage());
        }

        log.error("=================================");

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Invalid Request");
        response.put("message", detailedMessage);
        if (!fieldInfo.isEmpty()) {
            response.put("details", fieldInfo);
        }

        return ResponseEntity.badRequest().body(response);
    }

}
