package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.enums.ApiStatus;
import lombok.Getter;

@Getter
public class CommonResponse<T> {
    public ApiStatus status;
    public String message;
    public T data;

    private CommonResponse(ApiStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>(
                ApiStatus.SUCCESS,
                message,
                data
        );
    }

    public CommonResponse<T> success(T data) {
        return new CommonResponse<>(
                ApiStatus.SUCCESS,
                ApiStatus.SUCCESS.getDescription(),
                data
        );
    }

    public CommonResponse<T> noData() {
        return new CommonResponse<>(
                ApiStatus.NO_DATA,
                ApiStatus.NO_DATA.getDescription(),
                null
        );
    }

    public CommonResponse<T> badRequest(ApiStatus status, String message) {
        return new CommonResponse<>(
                status,
                message,
                null
        );
    }
}
