package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiscodeitException extends RuntimeException {

    private final Instant timestamp = Instant.now();
    private final ErrorCode errorCode;
    private final Map<String, Object> details;
}
