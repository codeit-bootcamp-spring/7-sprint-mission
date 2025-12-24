package com.sprint.mission.discodeit.common.exception.auth;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class InvalidAuthRequestException extends AuthException {
    public InvalidAuthRequestException(String reason) {
        super(ErrorCode.AUTH_INVALID_REQUEST, Map.of("reason", reason));
    }
}
