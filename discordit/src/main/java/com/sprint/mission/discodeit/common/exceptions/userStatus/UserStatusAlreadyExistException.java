package com.sprint.mission.discodeit.common.exceptions.userStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.entity.User;

public class UserStatusAlreadyExistException extends UserStatusException {
    public UserStatusAlreadyExistException(User user) {
        super(null, ErrorCode.ALREADY_EXISTS);
        this.details.put("userUuid", user.getId());
        this.details.put("username", user.getUsername());
    }
}
