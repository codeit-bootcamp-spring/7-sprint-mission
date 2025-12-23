package com.sprint.mission.discodeit.common.exceptions;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class DiscodeitException extends RuntimeException{
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    @Builder
    public DiscodeitException(ErrorCode errorCode) {
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = new HashMap<>();
    }

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
