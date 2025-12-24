package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class DuplicateReadStatusException extends ReadStatusException{
    public DuplicateReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
