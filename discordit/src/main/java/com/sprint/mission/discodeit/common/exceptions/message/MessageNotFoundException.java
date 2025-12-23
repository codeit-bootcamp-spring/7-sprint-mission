package com.sprint.mission.discodeit.common.exceptions.message;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends DiscodeitException {
    public MessageNotFoundException(UUID id) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails("messageId", id));
    }

    public MessageNotFoundException(String customMessage) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails("message", customMessage));
    }

    private static Map<String, Object> createDetails(String key, Object value) {
        Map<String, Object> details = new HashMap<>();
        details.put(key, value);
        details.put("resource", "Message");
        return details;
    }
}
