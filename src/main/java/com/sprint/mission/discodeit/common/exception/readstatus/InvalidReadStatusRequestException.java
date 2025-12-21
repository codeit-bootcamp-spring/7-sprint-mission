package com.sprint.mission.discodeit.common.exception.readstatus;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class InvalidReadStatusRequestException extends ReadStatusException {
    public InvalidReadStatusRequestException(String reason) {
        super(ErrorCode.INVALID_READ_STATUS_REQUEST, Map.of("reason", reason));
    }
}
