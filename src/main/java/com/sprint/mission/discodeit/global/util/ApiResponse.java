package com.sprint.mission.discodeit.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class ApiResponse<T> {
    private final int status;
    private final String code;
    private final String message;
    private final T data;
    private final Instant timestamp = Instant.now();

    private ApiResponse(int status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "SUCCESS", "요청에 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), "SUCCESS", "요청에 성공했습니다.", null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String code, String message) {
        return new ApiResponse<>(status.value(), code, message, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), status.name(), message, null);
    }
}
