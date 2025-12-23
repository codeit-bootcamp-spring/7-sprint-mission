package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateNameException extends UserException {
    public DuplicateNameException(String username) {
        super(ErrorCode.DUPLICATE_USER_NAME, Map.of("username", username));
    }
}
