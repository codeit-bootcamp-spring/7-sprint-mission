package com.sprint.mission.discodeit.common.exception.channel;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class InvalidChannelException extends ChannelException {
    public InvalidChannelException(String reason) {
        super(ErrorCode.INVALID_CHANNEL_REQUEST, Map.of("reason", reason));
    }
}
