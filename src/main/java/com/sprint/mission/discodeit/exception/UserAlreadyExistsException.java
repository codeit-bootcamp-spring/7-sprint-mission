package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.exception.ErrorCode.CHANNEL_NOT_FOUND;
import static com.sprint.mission.discodeit.exception.ErrorCode.DUPLICATE_USER;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class UserAlreadyExistsException extends UserException {

    public <T> UserAlreadyExistsException(String key, T tValue) {

        super( DUPLICATE_USER,
            Map.of(key, tValue));
//            Map.of("userId", userId));
    }
}
