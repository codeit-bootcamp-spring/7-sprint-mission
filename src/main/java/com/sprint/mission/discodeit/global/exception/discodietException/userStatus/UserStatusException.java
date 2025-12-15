package com.sprint.mission.discodeit.global.exception.discodietException.userStatus;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class UserStatusException extends DiscodeitException {

    public UserStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserStatusException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
