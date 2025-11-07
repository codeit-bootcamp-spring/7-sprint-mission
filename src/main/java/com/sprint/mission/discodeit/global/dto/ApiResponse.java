package com.sprint.mission.discodeit.global.dto;

import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final HttpStatus status;
    private final String message;
    private final T data;

    // 성공 응답(데이터 포함)
    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        return ResponseEntity
                .status(status)
                .body(ApiResponse.<T>builder()
                        .status(status)
                        .message(message)
                        .data(data)
                        .build());
    }

    // 성공 응답(데이터 데이터 없음)
    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(ApiResponse.<T>builder()
                        .status(status)
                        .message(message)
                        .data(null)
                        .build());
    }


    // 실패 응답(커스텀 예외 결과 전달)
    public static <T> ResponseEntity<ApiResponse<T>> error(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.<T> builder()
                        .status(e.getErrorCode().getStatus())
                        .message(e.getErrorCode().getMessage())
                        .data(null)
                        .build());
    }

    // 실패 응답(스프링 기본 예외 결과 전달)
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(ApiResponse.<T> builder()
                        .status(status)
                        .message(message)
                        .data(null)
                        .build());
    }
}
