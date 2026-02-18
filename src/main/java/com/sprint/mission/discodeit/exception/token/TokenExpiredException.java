package com.sprint.mission.discodeit.exception.token;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class TokenExpiredException extends TokenException{
    public TokenExpiredException(String message) {
        super(ErrorCode.EXPIRED_TOKEN, Map.of("message", message));
    }
}
