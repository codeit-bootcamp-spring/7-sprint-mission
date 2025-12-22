package com.sprint.mission.discodeit.global.exception.userstatus;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public abstract class UserStatusException extends DiscodeitException {
    public UserStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
