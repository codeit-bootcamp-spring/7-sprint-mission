package com.sprint.mission.discodeit.common.exceptions.binaryContent.s3;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class S3UploadException extends S3Exception{
    public S3UploadException(UUID id) {
        super(id, ErrorCode.UPLOAD_FAILED);
    }
}
