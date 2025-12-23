package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class StatusAlreadyExistsException extends UserStatusException {
    public StatusAlreadyExistsException(UUID id) {
        super(ErrorCode.STATUS_ALREADY_EXISTS, Map.of("id", id));
    }
}
