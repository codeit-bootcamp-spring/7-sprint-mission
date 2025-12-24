package com.sprint.mission.discodeit.common.exception.message;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class InvalidMessageRequestException extends MessageException {
    public InvalidMessageRequestException(String reason) {
        super(ErrorCode.INVALID_MESSAGE_REQUEST);
    }
}
