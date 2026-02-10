package com.sprint.mission.discodeit.exception.token;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class TokenInvalidException extends TokenException{
    public TokenInvalidException(String message) {
        super(ErrorCode.INVALID_TOKEN, Map.of("message", message));
    }
}
