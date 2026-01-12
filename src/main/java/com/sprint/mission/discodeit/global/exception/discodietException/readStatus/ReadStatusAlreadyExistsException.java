package com.sprint.mission.discodeit.global.exception.discodietException.readStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException {
    public ReadStatusAlreadyExistsException(String key, Object value) {
        super(ErrorCode.READ_STATUS_ALREADY_EXIST, key, value);
    }

    public static ReadStatusAlreadyExistsException byUserAndChannelId(UUID userId, UUID channelId) {
        String key = userId + " " + channelId;
        return new ReadStatusAlreadyExistsException("userid, channelId", key);
    }


}
