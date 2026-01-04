package com.sprint.mission.discodeit.global.exception.discodietException.userStatus;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class UserStatusException extends DiscodeitException {

    public UserStatusException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public UserStatusException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
