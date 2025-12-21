package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class UserException  extends DiscodeitException {

    public UserException(
        ErrorCode errorCode,
        Map<String, Object> details) {

        super(errorCode, details);
    }
}
