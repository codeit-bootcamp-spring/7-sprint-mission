package com.sprint.mission.discodeit.exception.domain.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class AuthException extends DiscodeitException {

    AuthException(ErrorCode errorCode, Map<String, Object> details){
        super(errorCode,details);
    }
}
