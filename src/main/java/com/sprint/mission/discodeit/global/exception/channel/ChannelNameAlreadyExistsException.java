package com.sprint.mission.discodeit.global.exception.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelNameAlreadyExistsException extends ChannelException {
    public ChannelNameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChannelNameAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
