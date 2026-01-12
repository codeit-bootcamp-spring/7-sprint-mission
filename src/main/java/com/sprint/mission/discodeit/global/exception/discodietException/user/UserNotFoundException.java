package com.sprint.mission.discodeit.global.exception.discodietException.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.List;
import java.util.UUID;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String key, Object value) {
        super(ErrorCode.USER_NOT_FOUND, key, value);
    }

    public static UserNotFoundException byId(UUID userId) {
        return new UserNotFoundException("userId", userId);
    }

    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("username", username);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("email", email);
    }

    public static UserNotFoundException byIds(List<UUID> userIds) {
        return new UserNotFoundException("userIds", userIds);
    }
}
