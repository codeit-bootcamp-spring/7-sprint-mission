package com.sprint.mission.discodeit.exception.domain.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class MessageException extends DiscodeitException {

    MessageException(ErrorCode errorCode, Map<String, Object> details){
        super(errorCode,details);
    }
}
