package com.sprint.mission.discodeit.exception.domain.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserNotJoinException extends UserException{
    public UserNotJoinException(UUID channelId,UUID userId){

        super(ErrorCode.USER_NOT_JOIN,new HashMap<>() {{
            put("userId", userId);
            put("channelId", channelId);
        }});
    }
}
