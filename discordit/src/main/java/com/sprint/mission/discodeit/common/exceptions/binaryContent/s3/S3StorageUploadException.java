package com.sprint.mission.discodeit.common.exceptions.binaryContent.s3;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class S3StorageUploadException extends S3StorageException {
    public S3StorageUploadException(UUID id) {
        super(id, ErrorCode.UPLOAD_FAILED);
    }
}
