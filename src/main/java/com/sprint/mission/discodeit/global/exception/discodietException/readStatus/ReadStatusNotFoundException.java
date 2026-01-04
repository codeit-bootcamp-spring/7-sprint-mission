package com.sprint.mission.discodeit.global.exception.discodietException.readStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {
    public ReadStatusNotFoundException(String key, Object value) {
        super(ErrorCode.READ_STATUS_NOT_FOUND, key, value);
    }

    public static ReadStatusNotFoundException byId(UUID readStatusId) {
        return new ReadStatusNotFoundException("readStatusId", readStatusId);
    }
}
