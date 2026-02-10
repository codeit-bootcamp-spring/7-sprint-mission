package com.sprint.mission.discodeit.common.exceptions.user;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String userId) {
        super(null, ErrorCode.NOT_FOUND);
        this.details.put("userId", userId);
    }

    public UserNotFoundException(UUID uuid) {
        super(uuid, ErrorCode.NOT_FOUND);
    }
}
