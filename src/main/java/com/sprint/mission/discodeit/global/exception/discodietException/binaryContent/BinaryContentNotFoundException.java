package com.sprint.mission.discodeit.global.exception.discodietException.binaryContent;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {
    public BinaryContentNotFoundException(String key, Object value) {
        super(ErrorCode.BINARY_CONTENT_NOT_FOUND, key, value);
    }

    public static BinaryContentNotFoundException byId(UUID binaryContentId) {
        return new BinaryContentNotFoundException("binaryContentId", binaryContentId);

    }
}
