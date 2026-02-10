package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BinaryContentException extends DiscodeitException {
    public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
        super(BinaryContent.class, errorCode, details);
    }

    public BinaryContentException(UUID id, ErrorCode errorCode) {
        super(BinaryContent.class, errorCode, new HashMap<>());
        details.put("contentId", id);
    }

    public BinaryContentException(String fileName, ErrorCode errorCode) {
        super(BinaryContent.class, errorCode, new HashMap<>());
        details.put("fileName", fileName);
    }
}
