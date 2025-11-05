package com.sprint.mission.discodeit.exception;

public record ErrorInfoRes (
        String code,
        String message,
        int status
){
    public static ErrorInfoRes of(ErrorCode errorCode){
        return new ErrorInfoRes(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
    }
}
