package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileUploadLimitExceedException extends BinaryContentException {
    public FileUploadLimitExceedException() {
        super(ErrorCode.FILE_UPLOAD_LIMIT_EXCEED);
    }
}
