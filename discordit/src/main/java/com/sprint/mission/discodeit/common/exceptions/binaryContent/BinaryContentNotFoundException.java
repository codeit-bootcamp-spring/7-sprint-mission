package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class BinaryContentNotFoundException extends DiscodeitException {

    public BinaryContentNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
        super(ErrorCode.NOT_FOUND, details);
    }
}
