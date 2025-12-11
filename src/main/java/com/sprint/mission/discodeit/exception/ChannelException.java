package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class ChannelException extends DiscodeitException{
    public ChannelException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }

    public ChannelException(String message, Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(message, timestamp, errorCode, details);
    }

    public ChannelException(String message, Throwable cause, Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(message, cause, timestamp, errorCode, details);
    }
}
