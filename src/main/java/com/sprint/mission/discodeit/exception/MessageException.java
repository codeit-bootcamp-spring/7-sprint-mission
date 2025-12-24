package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class MessageException  extends DiscodeitException {

    public MessageException(
        ErrorCode errorCode,
        Map<String, Object> details) {

        super(errorCode, details);
    }
}
