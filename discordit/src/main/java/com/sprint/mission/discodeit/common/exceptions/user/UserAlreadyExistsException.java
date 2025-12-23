package com.sprint.mission.discodeit.common.exceptions.user;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserAlreadyExistsException extends DiscodeitException {
    public UserAlreadyExistsException(String id) {
        super(Instant.now(), ErrorCode.ALREADY_EXISTS, createDetails(id));
    }

    private static Map<String, Object> createDetails(String id) {
        Map<String, Object> details = new HashMap<>();
        details.put("id", id);
        details.put("resource", "User");
        return details;
    }
}
