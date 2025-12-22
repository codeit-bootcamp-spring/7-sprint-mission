package com.sprint.mission.discodeit.exception.domain.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.UUID;

public class ChannelNotExistException extends ChannelException{

    public ChannelNotExistException(UUID channelId){
        super(ErrorCode.CHANNEL_NOT_EXIST,new HashMap<>(){{put("channelId",channelId);}});
    }
}
