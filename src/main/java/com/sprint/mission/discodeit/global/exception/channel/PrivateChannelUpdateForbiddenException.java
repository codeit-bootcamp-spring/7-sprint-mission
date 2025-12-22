package com.sprint.mission.discodeit.global.exception.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class PrivateChannelUpdateForbiddenException extends ChannelException {
    public PrivateChannelUpdateForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PrivateChannelUpdateForbiddenException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
