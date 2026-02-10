package com.sprint.mission.discodeit.common.exceptions.binaryContent.fileStorage;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentException;

import java.util.UUID;

public class FileStorageIOException extends BinaryContentException {
    public FileStorageIOException(UUID id) {
        super(id, ErrorCode.FILE_SAVE_FAILED);
    }
}
