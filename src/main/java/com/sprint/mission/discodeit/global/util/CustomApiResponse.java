package com.sprint.mission.discodeit.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

// sprint 5 미션 요구사항 중 Api 스펙을 맞추기 위해 사용하지 않음
@Getter
public class CustomApiResponse<T> {
    private final int status;
    private final String code;
    private final String message;
    private final T data;
    private final Instant timestamp = Instant.now();

    private CustomApiResponse(int status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomApiResponse<T> success(T data) {
        return new CustomApiResponse<>(HttpStatus.OK.value(), "SUCCESS", "요청에 성공했습니다.", data);
    }

    public static <T> CustomApiResponse<T> success() {
        return new CustomApiResponse<>(HttpStatus.OK.value(), "SUCCESS", "요청에 성공했습니다.", null);
    }

    public static <T> CustomApiResponse<T> error(HttpStatus status, String code, String message) {
        return new CustomApiResponse<>(status.value(), code, message, null);
    }

    public static <T> CustomApiResponse<T> error(HttpStatus status, String message) {
        return new CustomApiResponse<>(status.value(), status.name(), message, null);
    }
}
