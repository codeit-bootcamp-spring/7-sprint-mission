package com.sprint.mission.discodeit.common.exceptions.user;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserStatusNotExistException extends DiscodeitException {
    public UserStatusNotExistException(UserStatus userStatus) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails("userStatusId", userStatus.getId()));
    }

    public UserStatusNotExistException(UUID id) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails("userStatusId", id));
    }

    public UserStatusNotExistException(User user) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetailsWithUser(user));
    }

    private static Map<String, Object> createDetails(String key, Object value) {
        Map<String, Object> details = new HashMap<>();
        details.put(key, value);
        details.put("resource", "UserStatus");
        return details;
    }

    private static Map<String, Object> createDetailsWithUser(User user) {
        Map<String, Object> details = new HashMap<>();
        details.put("userId", user.getId());
        details.put("username", user.getUsername());
        details.put("resource", "UserStatus");
        return details;
    }
}
