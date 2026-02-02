package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(UUID id) {
        super(ErrorCode.USER_NOT_FOUND, Map.of("id", id));
    }

    private UserNotFoundException(String attribute, String message) {
        super(ErrorCode.USER_NOT_FOUND, Map.of(attribute, message));
    }
    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("email", email);
    }
    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("username", username);
    }
}
