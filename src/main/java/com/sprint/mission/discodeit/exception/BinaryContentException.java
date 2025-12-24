package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {

    public BinaryContentException(
        ErrorCode errorCode,
        Map<String, Object> details) {

        super(errorCode, details);
    }
}
