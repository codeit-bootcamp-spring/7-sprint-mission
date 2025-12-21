package com. sprint.mission.discodeit. exception.user;

import com. sprint.mission.discodeit. exception.ErrorCode;
import java.util. Map;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(String field, String value) {
        super(field. equals("email") ? ErrorCode.DUPLICATE_EMAIL : ErrorCode.DUPLICATE_USERNAME,
                Map.of(field, value));
    }
}