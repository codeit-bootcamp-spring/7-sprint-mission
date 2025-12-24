package com.sprint.mission.discodeit.common.exceptions.user;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends DiscodeitException {

    public UserNotFoundException(String id) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails("id", id));
    }

    public UserNotFoundException(User user) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails("userId", user.getId()));
    }

    public UserNotFoundException(UUID uuid) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails("userId", uuid));
    }

    private static Map<String, Object> createDetails(String key, Object value) {
        Map<String, Object> details = new HashMap<>();
        details.put(key, value);
        details.put("resource", "User");
        return details;
    }
}
