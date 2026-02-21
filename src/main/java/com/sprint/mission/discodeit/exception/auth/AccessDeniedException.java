package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class AccessDeniedException extends AuthException{
    public AccessDeniedException(String message) {
        super(ErrorCode.ACCESS_DENIED, Map.of("message", message));
    }
}
