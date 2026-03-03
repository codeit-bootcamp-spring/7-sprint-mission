package com.sprint.mission.discodeit.common.exceptions.auth;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

public class TokenNotValidException extends AuthException {
    public TokenNotValidException(String refreshToken) {
        super(ErrorCode.INVALID_TOKEN, refreshToken);
    }
}
