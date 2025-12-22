package com.sprint.mission.discodeit.exception.domain.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class UserNotExistException extends UserException{

    public UserNotExistException(UUID userId){
        super(ErrorCode.USER_NOT_EXIST, new HashMap<>(){{put("userId",userId);}});

    }

}
