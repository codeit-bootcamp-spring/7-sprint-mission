package com.sprint.mission.discodeit.exception.domain.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class MessageNotExistException extends MessageException{
    public MessageNotExistException(UUID messageId){
        super(ErrorCode.MESSAGE_NOT_EXIST,new HashMap<>(){{put("messageId",messageId);}});
    }
}
