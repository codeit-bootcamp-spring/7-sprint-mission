package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class BinaryContentAlreadyExistException extends BinaryContentException {
    public BinaryContentAlreadyExistException(UUID id) {
        super(id, ErrorCode.ALREADY_EXISTS);
    }

    public BinaryContentAlreadyExistException(String fileName) {
        super(fileName, ErrorCode.ALREADY_EXISTS);
    }
}
