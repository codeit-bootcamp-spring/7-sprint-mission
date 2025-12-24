package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.exception.ErrorCode.USERSTATUS_NOT_FOUND;

import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {

    public UserStatusNotFoundException(UUID userId) {

        super(USERSTATUS_NOT_FOUND,
            Map.of("userId", userId));
    }
}
