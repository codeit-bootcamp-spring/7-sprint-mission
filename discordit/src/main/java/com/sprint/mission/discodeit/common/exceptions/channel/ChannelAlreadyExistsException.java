package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class ChannelAlreadyExistsException extends DiscodeitException {
    public ChannelAlreadyExistsException(Map<String, Object> details) {
        super(ErrorCode.ALREADY_EXISTS, details);
    }
}
