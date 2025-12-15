package com.sprint.mission.discodeit.global.exception.discodietException.readStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException {
    public ReadStatusAlreadyExistsException() {
        super(ErrorCode.READ_STATUS_ALREADY_EXIST);
    }

    public static ReadStatusAlreadyExistsException byId(UUID readStatusId) {
        ReadStatusAlreadyExistsException readStatusAlreadyException = new ReadStatusAlreadyExistsException();
        readStatusAlreadyException.updateDetail("readStatusId", readStatusId);
        return readStatusAlreadyException;
    }


}
