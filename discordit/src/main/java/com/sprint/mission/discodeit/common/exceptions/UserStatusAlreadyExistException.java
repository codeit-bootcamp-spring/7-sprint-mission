package com.sprint.mission.discodeit.common.exceptions;

import com.sprint.mission.discodeit.entity.User;

public class UserStatusAlreadyExistException extends RuntimeException {
    public UserStatusAlreadyExistException(User user) {
        super(user.getUserId() + "의 UserStatus는 이미 존재합니다.");
    }
}
