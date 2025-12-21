package com.sprint.mission.discodeit.common.exception.userstatus;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
    public UserStatusNotFoundException(UUID userStatusId) {
        super(ErrorCode.USER_STATUS_NOT_FOUND, Map.of("userStatusId", userStatusId));
    }
}
