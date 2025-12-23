package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class StatusNotFoundException extends UserStatusException {
    public StatusNotFoundException(UUID userId) {
        super(ErrorCode.STATUS_NOT_FOUND, Map.of("userId", userId));
    }
}
