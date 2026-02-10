package com.sprint.mission.discodeit.common.exceptions.user;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class UserAlreadyExistsException extends UserException {
    public UserAlreadyExistsException(UUID id, String userId) {
        super(id, ErrorCode.ALREADY_EXISTS);
        this.getDetails().put("id", userId);
    }
}
