package com.sprint.mission.discodeit.common.exception.message;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class SlowModeViolationException extends MessageException {
    public SlowModeViolationException(UUID channelId, UUID authorId, long waitSeconds) {
        super(ErrorCode.SLOW_MODE_VIOLATION, Map.of(
                "channelId", channelId, "authorId", authorId, "waitSeconds", waitSeconds));
    }
}
