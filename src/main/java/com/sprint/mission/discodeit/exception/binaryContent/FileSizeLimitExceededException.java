package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileSizeLimitExceededException extends BinaryContentException {
    public FileSizeLimitExceededException() {
        super(ErrorCode.FILE_SIZE_LIMIT_EXCEED);
    }
}
