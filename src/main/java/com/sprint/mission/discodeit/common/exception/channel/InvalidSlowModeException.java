package com.sprint.mission.discodeit.common.exception.channel;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class InvalidSlowModeException extends ChannelException {
    public InvalidSlowModeException(UUID channelId, int slowModeSeconds) {
        super(ErrorCode.INVALID_SLOW_MODE, Map.of("channelId", channelId, "slowMode", slowModeSeconds));
    }
}
