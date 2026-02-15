package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.exception.ErrorCode.JWT_UNAUTHORIZED;

import java.util.Map;

public class UnAuthorizedException extends UserStatusException {

    public UnAuthorizedException(String msg) {
        super(JWT_UNAUTHORIZED,
            Map.of("msg", msg));
    }
}
