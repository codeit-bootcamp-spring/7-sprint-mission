package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChannelException extends DiscodeitException {
    public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
        super(Channel.class, errorCode, details);
    }

    public ChannelException(UUID id, ErrorCode errorCode) {
        super(Channel.class, errorCode, new HashMap<>());
        details.put("contentId", id);
    }
}
