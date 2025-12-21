package com.sprint.mission.discodeit.common.exception.binarycontent;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class InvalidBinaryContentRequestException extends BinaryContentException {
    public InvalidBinaryContentRequestException(String reason) {
        super(ErrorCode.INVALID_BINARY_CONTENT_REQUEST, Map.of("reason", reason));
    }
}
