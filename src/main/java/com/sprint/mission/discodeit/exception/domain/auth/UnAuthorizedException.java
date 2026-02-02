package com.sprint.mission.discodeit.exception.domain.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class UnAuthorizedException extends AuthException{

   public UnAuthorizedException() {
        super(ErrorCode.ACCESS_DENIED_ERROR,new HashMap<>());
    }
}
