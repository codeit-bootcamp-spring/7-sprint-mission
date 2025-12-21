package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.exception.ErrorCode.USER_NOT_FOUND;

import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(UUID userId) {
        super(USER_NOT_FOUND,
            Map.of("userId", userId));
    }

    public UserNotFoundException(String userName) {
        super(USER_NOT_FOUND,
            Map.of("userName", userName));
    }
}
