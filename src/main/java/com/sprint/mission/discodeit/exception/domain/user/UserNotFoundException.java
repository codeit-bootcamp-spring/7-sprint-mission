package com.sprint.mission.discodeit.exception.domain.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class UserNotFoundException extends UserException{

    UserNotFoundException(UUID userId){
        super(ErrorCode.USER_NOT_FOUND, new HashMap<>(){{put("userId",userId);}});

    }

}
