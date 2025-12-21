package com.sprint.mission.discodeit.common.exception.binarycontent;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class BinaryContentStorageException extends BinaryContentException {
    public BinaryContentStorageException(ErrorCode errorCode, UUID binaryContentId, Throwable cause) {
        super(errorCode, Map.of("binaryContentId", binaryContentId), cause);
    }
}
