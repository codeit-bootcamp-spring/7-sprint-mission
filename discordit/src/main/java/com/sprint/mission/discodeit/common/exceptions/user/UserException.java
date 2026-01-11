package com.sprint.mission.discodeit.common.exceptions.user;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public class UserException extends DiscodeitException {
    public UserException(ErrorCode errorCode, Map<String, Object> details) {
        super(User.class, errorCode, details);
    }

    public UserException(UUID id, ErrorCode errorCode) {
        super(User.class, errorCode);
        details.put("contentId", id);
    }
}
