package com.sprint.mission.discodeit.exception.domain.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class InSufficientAccessException extends AuthException {

    public InSufficientAccessException(String username) {
        super(ErrorCode.ACCESS_DENIED_ERROR,new HashMap<>(){{put("username",username);}});
    }
}
