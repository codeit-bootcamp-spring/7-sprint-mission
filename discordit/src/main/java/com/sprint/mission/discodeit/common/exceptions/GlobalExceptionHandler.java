package com.sprint.mission.discodeit.common.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.sprint.mission.discodeit.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.INVALID_ARGS.name())
                .message(ErrorCode.INVALID_ARGS.getDescription())
                .details(errors)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentsExceptions(
            IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.ILLEGAL_ARGUMENT.name())
                .message(ErrorCode.ILLEGAL_ARGUMENT.getDescription())
                .details(details)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());
        details.put("cause", ex.getCause() != null ? ex.getCause().toString() : null);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getDescription())
                .details(details)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String detailedMessage = "요청 본문을 읽을 수 없습니다.";
        Map<String, Object> details = new HashMap<>();

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

            details.put("field", fieldPath);
            details.put("expectedType", targetType.getSimpleName());
            detailedMessage = String.format("'%s' 필드의 형식이 올바르지 않습니다. %s 타입이어야 합니다.",
                    fieldPath, targetType.getSimpleName());

            log.error("파싱된 정보 - 필드: {}, 예상 타입: {}", fieldPath, targetType.getSimpleName());
        } else if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidEx = (InvalidFormatException) cause;
            String fieldPath = invalidEx.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(name -> name != null && !name.isEmpty())
                    .collect(Collectors.joining("."));

            if (fieldPath.isEmpty()) {
                fieldPath = "알 수 없는 필드";
            }

            details.put("field", fieldPath);
            details.put("invalidValue", invalidEx.getValue());
            details.put("expectedType", invalidEx.getTargetType().getSimpleName());
            detailedMessage = String.format("'%s' 필드의 값 '%s'이(가) 올바르지 않습니다.",
                    fieldPath, invalidEx.getValue());

            log.error("파싱된 정보 - 필드: {}, 입력값: {}, 예상 타입: {}",
                    fieldPath, invalidEx.getValue(), invalidEx.getTargetType().getSimpleName());
        } else {
            log.error("알 수 없는 파싱 오류 | 원본 메시지: {}", ex.getMessage());
            details.put("originalMessage", ex.getMessage());
        }

        log.error("=================================");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.INVALID_REQUEST_BODY.name())
                .message(detailedMessage)
                .details(details)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
        log.error("DiscodeitException: {}", ex.getErrorCode(), ex);

        ErrorResponse errorResponse = ex.toErrorResponse();

        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("message", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .message("NPE가 발생했습니다.")
                .details(details)
                .exceptionType(ex.getClass().getSimpleName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
