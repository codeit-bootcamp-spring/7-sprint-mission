package com.sprint.mission.discodeit.common.exceptions.message;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public class MessageException extends DiscodeitException {
    public MessageException(ErrorCode errorCode, Map<String, Object> details) {
        super(Message.class, errorCode, details);
    }

    public MessageException(UUID id, ErrorCode errorCode) {
        super(Message.class, errorCode);
        details.put("contentId", id);
    }
}
