package com.sprint.mission.discodeit.common.response;

import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T>{
    //Fields
    private final String code;
    private final String message;
    private final T data;

    //Static Factory Method
    //성공 응답(데이터 포함
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>("SUCCESS", "요청에 성공했습니다.", data);
    }
    
    //성공 응답(데이터 없음)
    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>("SUCCESS", "요청에 성공했습니다.(반환 데이터 없음)", null);
    }

    //실패 응답
    public static ApiResponse<String> fail(ErrorCode errorCode){
        return new ApiResponse<>(
                errorCode.getCode(),
                errorCode.getMessage(),
                "HttpStatus - " + errorCode.getStatus().value()
        );
    }
}
