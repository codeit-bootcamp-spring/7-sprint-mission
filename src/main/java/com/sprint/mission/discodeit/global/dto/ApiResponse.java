package com.sprint.mission.discodeit.global.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public record ApiResponse<T> (
        HttpStatus status,
        String message,
        T data
) {
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
}
