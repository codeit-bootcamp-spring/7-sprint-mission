package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class UserStatusException extends DiscodeitException{

    public UserStatusException(
        ErrorCode errorCode,
        Map<String, Object> details
    ){
        super(errorCode, details);
    }
}
