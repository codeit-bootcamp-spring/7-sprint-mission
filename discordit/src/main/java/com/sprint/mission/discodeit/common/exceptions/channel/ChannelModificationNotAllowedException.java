package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelModificationNotAllowedException extends ChannelException {
    public ChannelModificationNotAllowedException(UUID id, Map<String, Object> details) {
        super(ErrorCode.ALREADY_EXISTS, details);
        this.getDetails().put("id", id);
    }

    public ChannelModificationNotAllowedException(UUID id) {
        super(id, ErrorCode.ALREADY_EXISTS);
    }
}
