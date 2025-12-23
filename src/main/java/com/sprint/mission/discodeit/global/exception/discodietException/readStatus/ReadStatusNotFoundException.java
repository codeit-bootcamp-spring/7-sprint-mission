package com.sprint.mission.discodeit.global.exception.discodietException.readStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {
    public ReadStatusNotFoundException() {
        super(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    public static ReadStatusNotFoundException byId(UUID readStatusId) {
        ReadStatusNotFoundException readStatusNotFoundException = new ReadStatusNotFoundException();
        readStatusNotFoundException.updateDetail("readStatusId", readStatusId);
        return readStatusNotFoundException;
    }
}
