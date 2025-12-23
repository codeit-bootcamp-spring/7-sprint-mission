package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ReadNotFoundException extends ReadStatusException {
    public ReadNotFoundException(UUID id) {
        super(ErrorCode.READ_NOT_FOUND, Map.of("id", id));
    }
}
