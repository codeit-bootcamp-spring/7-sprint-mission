package com.sprint.mission.discodeit.exception.domain.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class UserStatusNotMatchException extends UserStatusException{

    public UserStatusNotMatchException(UUID userId){
        super(ErrorCode.USERSTATUS_NOT_MATCH, new HashMap<>(){{put("userId",userId);}});
    }
}
