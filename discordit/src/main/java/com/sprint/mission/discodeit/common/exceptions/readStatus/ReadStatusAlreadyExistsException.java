package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException {
    public ReadStatusAlreadyExistsException(UUID id) {
        super(id, ErrorCode.ALREADY_EXISTS);
    }
}
