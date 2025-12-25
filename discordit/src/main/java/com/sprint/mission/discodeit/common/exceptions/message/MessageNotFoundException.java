package com.sprint.mission.discodeit.common.exceptions.message;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class MessageNotFoundException extends DiscodeitException {

    public MessageNotFoundException(Map<String, Object> details) {
        super(ErrorCode.NOT_FOUND, details);
    }
}
