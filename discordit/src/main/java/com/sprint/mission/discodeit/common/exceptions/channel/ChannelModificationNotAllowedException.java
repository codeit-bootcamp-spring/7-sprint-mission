package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class ChannelModificationNotAllowedException extends DiscodeitException {
    public ChannelModificationNotAllowedException(Map<String, Object> details) {
        super(ErrorCode.CHANNEL_MODIFICATION_NOT_ALLOWED, details);
    }
}
