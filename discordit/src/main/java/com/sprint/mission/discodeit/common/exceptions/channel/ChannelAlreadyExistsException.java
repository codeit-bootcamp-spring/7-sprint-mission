package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChannelAlreadyExistsException extends DiscodeitException {
    public ChannelAlreadyExistsException(UUID uuid) {
        super(Instant.now(), ErrorCode.ALREADY_EXISTS, createDetails("channelId", uuid));
    }

    public ChannelAlreadyExistsException(String message) {
        super(Instant.now(), ErrorCode.ALREADY_EXISTS, createDetails("message", message));
    }

    private static Map<String, Object> createDetails(String key, Object value) {
        Map<String, Object> details = new HashMap<>();
        details.put(key, value);
        details.put("resource", "Channel");
        return details;
    }
}
