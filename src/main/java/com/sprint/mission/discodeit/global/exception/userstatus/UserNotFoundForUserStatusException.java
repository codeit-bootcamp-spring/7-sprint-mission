package com.sprint.mission.discodeit.global.exception.userstatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UserNotFoundForUserStatusException extends UserStatusException {
    public UserNotFoundForUserStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotFoundForUserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
