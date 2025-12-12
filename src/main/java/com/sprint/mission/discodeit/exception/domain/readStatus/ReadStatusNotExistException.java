package com.sprint.mission.discodeit.exception.domain.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class ReadStatusNotExistException extends ReadStatusException{

    public ReadStatusNotExistException(UUID readStatusId){
        super(ErrorCode.READSTATUS_NOT_EXIST,new HashMap<>(){{put("readStatusId",readStatusId);}});
    }
}
