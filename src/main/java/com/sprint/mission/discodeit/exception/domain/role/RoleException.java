package com.sprint.mission.discodeit.exception.domain.role;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class RoleException extends DiscodeitException {

    RoleException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
