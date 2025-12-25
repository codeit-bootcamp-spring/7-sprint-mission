package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class ReadStatusAlreadyExistsException extends DiscodeitException {
    public ReadStatusAlreadyExistsException(Map<String, Object> details) {
        super(ErrorCode.ALREADY_EXISTS, details);
    }
}
