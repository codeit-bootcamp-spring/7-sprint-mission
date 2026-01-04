package com.sprint.mission.discodeit.global.exception.discodietException.message;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException(String key, Object value) {
        super(ErrorCode.MESSAGE_NOT_FOUND, key, value);
    }

    public static MessageNotFoundException byId(UUID messageId) {
        return new MessageNotFoundException("messageId", messageId);
    }
}
