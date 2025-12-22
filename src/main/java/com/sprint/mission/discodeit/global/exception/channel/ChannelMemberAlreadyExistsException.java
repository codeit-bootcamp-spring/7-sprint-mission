package com.sprint.mission.discodeit.global.exception.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelMemberAlreadyExistsException extends ChannelException {
    public ChannelMemberAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChannelMemberAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
