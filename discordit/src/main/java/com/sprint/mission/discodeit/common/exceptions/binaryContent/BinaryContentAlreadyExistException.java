package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BinaryContentAlreadyExistException extends DiscodeitException {
    public BinaryContentAlreadyExistException(BinaryContent content) {
        super(Instant.now(), ErrorCode.ALREADY_EXISTS, createDetails(content.getId()));
    }

    public BinaryContentAlreadyExistException(UUID id) {
        super(Instant.now(), ErrorCode.ALREADY_EXISTS, createDetails(id));
    }

    private static Map<String, Object> createDetails(UUID binaryContentId) {
        Map<String, Object> details = new HashMap<>();
        details.put("binaryContentId", binaryContentId);
        details.put("resource", "BinaryContent");
        return details;
    }
}
