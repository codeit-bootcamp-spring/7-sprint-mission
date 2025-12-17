package com.sprint.mission.discodeit.global.exception.discodietException.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.List;
import java.util.UUID;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public static UserNotFoundException byId(UUID userId) {
        UserNotFoundException userNotFoundException = new UserNotFoundException();
        userNotFoundException.updateDetail("userId", userId);
        return userNotFoundException;
    }

    public static UserNotFoundException byUsername(String username) {
        UserNotFoundException userNotFoundException = new UserNotFoundException();
        userNotFoundException.updateDetail("username", username);
        return userNotFoundException;
    }

    public static UserNotFoundException byEmail(String email) {
        UserNotFoundException userNotFoundException = new UserNotFoundException();
        userNotFoundException.updateDetail("email", email);
        return userNotFoundException;
    }

    public static UserNotFoundException byIds(List<UUID> userIds) {
        UserNotFoundException userNotFoundException = new UserNotFoundException();
        userNotFoundException.updateDetail("userIds", userIds);
        return userNotFoundException;
    }
}
