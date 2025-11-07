package com.sprint.mission.discodeit.common.exceptions;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public class UserStatusNotExistException extends RuntimeException {
    public UserStatusNotExistException(UserStatus userStatus) {
        super("존재하지 않는 Userstatus입니다. : " + userStatus.getUuid());
    }

    public UserStatusNotExistException(UUID id) {
        super("존재하지 않는 Userstatus입니다. : " + id);
    }

    public UserStatusNotExistException(User user) {
        super("Userstatus가 존재하지 않는 유저입니다. : " + user.getUserId());
    }
}
