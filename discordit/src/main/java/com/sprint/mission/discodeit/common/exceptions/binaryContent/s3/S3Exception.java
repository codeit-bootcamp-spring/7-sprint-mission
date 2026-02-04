package com.sprint.mission.discodeit.common.exceptions.binaryContent.s3;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentException;

import java.util.Map;
import java.util.UUID;

public class S3Exception extends BinaryContentException {

    public S3Exception(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public S3Exception(UUID id, ErrorCode errorCode) {
        super(id, errorCode);
    }

    public S3Exception(String fileName, ErrorCode errorCode) {
        super(fileName, errorCode);
    }
}
