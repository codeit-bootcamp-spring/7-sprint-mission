package com.sprint.mission.discodeit.common.exceptions.binaryContent.s3;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

public class S3DownloadException extends S3Exception{
    public S3DownloadException(String fileName) {
        super(fileName, ErrorCode.DOWNLOAD_FAILED);
    }

}
