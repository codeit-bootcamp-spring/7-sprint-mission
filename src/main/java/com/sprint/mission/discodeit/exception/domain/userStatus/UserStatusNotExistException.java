package com.sprint.mission.discodeit.exception.domain.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class UserStatusNotExistException extends UserStatusException{

    public UserStatusNotExistException(UUID userStatusId){
        super(ErrorCode.USERSTATUS_NOT_EXIST,new HashMap<>(){
            {put("userStatusId",userStatusId);}
        });
    }
}
