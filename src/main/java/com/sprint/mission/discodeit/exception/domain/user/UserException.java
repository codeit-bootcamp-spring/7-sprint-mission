package com.sprint.mission.discodeit.exception.domain.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class UserException extends DiscodeitException {

    UserException(ErrorCode errorCode, Map<String, Object> details){
        super(errorCode,details);
    }
}

