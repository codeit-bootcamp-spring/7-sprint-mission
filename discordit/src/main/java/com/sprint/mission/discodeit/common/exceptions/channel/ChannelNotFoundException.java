package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException(UUID id, Map<String, Object> details) {
        super(ErrorCode.ALREADY_EXISTS, details);
        this.getDetails().put("id", id);
    }

    public ChannelNotFoundException(UUID id) {
        super(id, ErrorCode.ALREADY_EXISTS);
    }

}
