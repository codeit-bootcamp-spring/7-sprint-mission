package com.sprint.mission.discodeit.common.exceptions;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class DiscodeitException extends RuntimeException{
    private final Instant timestamp = Instant.now();
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public ErrorResponse toErrorResponse() {
        return ErrorResponse.builder()
                .timestamp(timestamp)
                .code(errorCode.name())
                .message(errorCode.getDescription())
                .details(details)
                .exceptionType(this.getClass().getSimpleName())
                .status(errorCode.getHttpStatus().value())
                .build();
    }
}
