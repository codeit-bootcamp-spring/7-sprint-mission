package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class DirectoryCreationFailed extends BinaryContentException {
    public DirectoryCreationFailed() {
        super(ErrorCode.DIRECTORY_CREATION_FAILED);
    }
}
