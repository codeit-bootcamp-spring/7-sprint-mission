package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(UUID id) {
        super(id, ErrorCode.FILE_NOT_FOUND);
    }

    public BinaryContentNotFoundException(String fileName) {
        super(fileName, ErrorCode.FILE_NOT_FOUND);
    }
}
