package com.sprint.mission.discodeit.common.exceptions.user;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserStatusAlreadyExistException extends DiscodeitException {
    public UserStatusAlreadyExistException(User user) {
        super(Instant.now(), ErrorCode.ALREADY_EXISTS, createDetails(user));
    }

    private static Map<String, Object> createDetails(User user) {
        Map<String, Object> details = new HashMap<>();
        details.put("userId", user.getId());
        details.put("username", user.getUsername());
        details.put("resource", "UserStatus");
        return details;
    }
}
