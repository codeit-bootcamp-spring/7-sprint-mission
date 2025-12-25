package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class ChannelNotFoundException extends DiscodeitException {
    public ChannelNotFoundException(Map<String, Object> details) {
        super(ErrorCode.NOT_FOUND, details);
    }

}
