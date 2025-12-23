package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class FileNotFoundException extends BinaryContentException {
    public FileNotFoundException(UUID id) {
        super(ErrorCode.FILE_NOT_FOUND,
                Map.of("id", id)
        );
    }
}
