package com.sprint.mission.discodeit.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorInfoRes {
    private final String code;
    private final String message;
    private final int status;

    public static ErrorInfoRes of(ErrorCode errorCode) {
        return ErrorInfoRes.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();
    }
}
