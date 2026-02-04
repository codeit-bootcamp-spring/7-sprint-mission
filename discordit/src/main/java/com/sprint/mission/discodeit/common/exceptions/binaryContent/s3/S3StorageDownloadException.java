package com.sprint.mission.discodeit.common.exceptions.binaryContent.s3;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

public class S3StorageDownloadException extends S3StorageException {
    public S3StorageDownloadException(String fileName) {
        super(fileName, ErrorCode.DOWNLOAD_FAILED);
    }

}
