package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class BinaryContentAlreadyExistException extends DiscodeitException {
    public BinaryContentAlreadyExistException(Map<String, Object> details) {
        super(ErrorCode.ALREADY_EXISTS, details);
    }
}
