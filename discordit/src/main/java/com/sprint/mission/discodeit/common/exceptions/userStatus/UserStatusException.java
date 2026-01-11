package com.sprint.mission.discodeit.common.exceptions.userStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserStatusException extends DiscodeitException {
    public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(UserStatus.class, errorCode, details);
    }

    public UserStatusException(UUID id, ErrorCode errorCode) {
        super(UserStatus.class, errorCode, new HashMap<>());
        details.put("contentId", id);
    }
}
