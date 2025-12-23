package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ChannelModificationNotAllowedException extends DiscodeitException {
    public ChannelModificationNotAllowedException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }

    public ChannelModificationNotAllowedException(UUID uuid) {
        super(ErrorCode.CHANNEL_MODIFICATION_NOT_ALLOWED);
        this.getDetails().put("요청 채널 uuid", uuid);

    }
}
