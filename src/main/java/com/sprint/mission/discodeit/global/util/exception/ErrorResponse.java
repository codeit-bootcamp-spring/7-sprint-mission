package com.sprint.mission.discodeit.global.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;
    private Instant timestamp;

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getStatus().value(),  // status
                errorCode.name(),               // code
                errorCode.getMessage(),         // message
                Instant.now()                   // timestamp
        );
    }
}
