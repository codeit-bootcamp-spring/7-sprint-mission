package com.sprint.mission.discodeit.global.exception.discodietException.message;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException() {
        super(ErrorCode.MESSAGE_NOT_FOUND);
    }

    public static MessageNotFoundException byId(UUID messageId) {
        MessageNotFoundException messageNotFoundException = new MessageNotFoundException();
        messageNotFoundException.updateDetail("messageId", messageId);
        return messageNotFoundException;
    }
}
