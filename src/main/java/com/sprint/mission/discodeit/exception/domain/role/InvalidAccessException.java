package com.sprint.mission.discodeit.exception.domain.role;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class InvalidAccessException extends RoleException{

    public InvalidAccessException(){
        super(ErrorCode.ACCESS_DENIED_ERROR,new HashMap<>());
    }
}
