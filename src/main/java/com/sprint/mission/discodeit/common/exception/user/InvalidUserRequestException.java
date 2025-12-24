package com.sprint.mission.discodeit.common.exception.user;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class InvalidUserRequestException extends UserException {
    public InvalidUserRequestException(String reason) {
        super(ErrorCode.INVALID_USER_REQUEST, Map.of("reason", reason));
    }
}
