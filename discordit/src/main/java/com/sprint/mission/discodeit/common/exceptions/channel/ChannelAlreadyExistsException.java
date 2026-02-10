package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class ChannelAlreadyExistsException extends ChannelException {
    public ChannelAlreadyExistsException(UUID id) {
        super(id, ErrorCode.ALREADY_EXISTS);
    }
}
