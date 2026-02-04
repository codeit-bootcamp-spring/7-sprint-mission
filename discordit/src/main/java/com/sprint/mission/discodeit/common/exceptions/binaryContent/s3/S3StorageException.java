package com.sprint.mission.discodeit.common.exceptions.binaryContent.s3;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentException;

import java.util.Map;
import java.util.UUID;

public class S3StorageException extends BinaryContentException {

    public S3StorageException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public S3StorageException(UUID id, ErrorCode errorCode) {
        super(id, errorCode);
    }

    public S3StorageException(String fileName, ErrorCode errorCode) {
        super(fileName, errorCode);
    }
}
