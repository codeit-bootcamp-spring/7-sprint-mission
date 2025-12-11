package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class MessageNotEmptyException extends MessageException {
    public MessageNotEmptyException() {
        super(ErrorCode.MESSAGE_NOT_EMPTY);
    }
    public MessageNotEmptyException(UUID id) {
        super(ErrorCode.MESSAGE_NOT_EMPTY, Map.of("id", id));
    }
}
