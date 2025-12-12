package com.sprint.mission.discodeit.global.exception.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class NotChannelMemberException extends ChannelException {
    public NotChannelMemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotChannelMemberException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
