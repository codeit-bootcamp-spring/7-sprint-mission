package com.sprint.mission.discodeit.exception;

public record ErrorInfoRes(
        String code,
        String message,
        int httpStatus
) {
    public static ErrorInfoRes from(ErrorCode errorCode) {
        return new ErrorInfoRes(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
    }
}
