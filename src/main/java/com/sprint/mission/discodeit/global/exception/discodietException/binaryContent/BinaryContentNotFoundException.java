package com.sprint.mission.discodeit.global.exception.discodietException.binaryContent;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {
    public BinaryContentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static BinaryContentNotFoundException byId(UUID binaryContentId) {
        BinaryContentNotFoundException binaryContentNotFoundException = new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND);
        binaryContentNotFoundException.updateDetail("binaryContentId", binaryContentId);
        return binaryContentNotFoundException;

    }
}
