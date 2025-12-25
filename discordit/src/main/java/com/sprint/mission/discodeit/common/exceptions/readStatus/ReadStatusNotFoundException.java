package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class ReadStatusNotFoundException extends DiscodeitException {
    public ReadStatusNotFoundException(Map<String, Object> details) {
        super(ErrorCode.NOT_FOUND, details);
    }
}
