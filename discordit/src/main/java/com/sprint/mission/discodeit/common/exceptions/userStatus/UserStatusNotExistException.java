package com.sprint.mission.discodeit.common.exceptions.userStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class UserStatusNotExistException extends UserStatusException {

    public UserStatusNotExistException(UUID id) {
        super(id, ErrorCode.NOT_FOUND);
    }

    public UserStatusNotExistException(User user) {
        super(null, ErrorCode.NOT_FOUND);
        this.details.put("userUuid", user.getId());
        this.details.put("username", user.getUsername());
    }
}
