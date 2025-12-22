package com.sprint.mission.discodeit.exception.domain.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.HashMap;

public class UserStatusException extends DiscodeitException {
    public UserStatusException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode,details);
    }
}
