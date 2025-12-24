package com.sprint.mission.discodeit.common.exceptions.binaryContent;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BinaryContentNotFoundException extends DiscodeitException {
    public BinaryContentNotFoundException(BinaryContent content) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails(content.getId()));
    }

    public BinaryContentNotFoundException(UUID id) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails(id));
    }

    private static Map<String, Object> createDetails(UUID binaryContentId) {
        Map<String, Object> details = new HashMap<>();
        details.put("binaryContentId", binaryContentId);
        details.put("resource", "BinaryContent");
        return details;
    }
}
