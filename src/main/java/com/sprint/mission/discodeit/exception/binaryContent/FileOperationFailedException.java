package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class FileOperationFailedException extends BinaryContentException {
    public FileOperationFailedException() {
        super(ErrorCode.FILE_OPERATION_FAILED);
    }

    public FileOperationFailedException(UUID id) {
        super(ErrorCode.FILE_OPERATION_FAILED, Map.of("id", id));
    }
}
