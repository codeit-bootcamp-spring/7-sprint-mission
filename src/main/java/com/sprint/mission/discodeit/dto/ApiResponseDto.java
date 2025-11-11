package com.sprint.mission.discodeit.dto;

import lombok.Getter;

@Getter
public class ApiResponseDto<T> {

    private final String code;
    private final String message;
    private final T responseData;

    private ApiResponseDto(String code, String message, T responseData) {
        this.code = code;
        this.message = message;
        this.responseData = responseData;
    }

    public static <T> ApiResponseDto<T> success (T responseData)
    {
        return new ApiResponseDto<>("SUCCESS","success message",responseData);
    }

    public static <T> ApiResponseDto<T> fail (String code,String message)
    {
        return new ApiResponseDto<T>(code,message,null);
    }
}
