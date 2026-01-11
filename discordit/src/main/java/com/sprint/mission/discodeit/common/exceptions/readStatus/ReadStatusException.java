package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Map;
import java.util.UUID;

public class ReadStatusException extends DiscodeitException {
    public ReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(ReadStatus.class, errorCode, details);
    }

    public ReadStatusException(UUID id, ErrorCode errorCode) {
        super(ReadStatus.class, errorCode);
        details.put("contentId", id);
    }
}
