package com.sprint.mission.discodeit.common.exception.userstatus;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class InvalidUserStatusRequestException extends UserStatusException {
    public InvalidUserStatusRequestException(String reason) {
        super(ErrorCode.INVALID_USER_STATUS_REQUEST, Map.of("reason", reason));
    }
}
