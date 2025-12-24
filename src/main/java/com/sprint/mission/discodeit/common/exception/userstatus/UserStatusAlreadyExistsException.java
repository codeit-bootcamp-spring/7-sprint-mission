package com.sprint.mission.discodeit.common.exception.userstatus;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {
    public UserStatusAlreadyExistsException(UUID userStatusId) {
        super(ErrorCode.USER_STATUS_ALREADY_EXISTS, Map.of("userStatusId", userStatusId));
    }
}
