package com.sprint.mission.discodeit.exception.domain.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class ChannelException extends DiscodeitException {

    ChannelException(ErrorCode errorCode, Map<String, Object> details){
        super(errorCode,details);
    }
}
