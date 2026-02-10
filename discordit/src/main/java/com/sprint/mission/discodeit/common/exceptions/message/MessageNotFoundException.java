package com.sprint.mission.discodeit.common.exceptions.message;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException(UUID id) {
        super(id, ErrorCode.NOT_FOUND);
    }
}
