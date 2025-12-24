package com.sprint.mission.discodeit.common.exceptions.channel;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends DiscodeitException {
    public ChannelNotFoundException(UUID uuid) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails(uuid));
    }

    private static Map<String, Object> createDetails(UUID channelId) {
        Map<String, Object> details = new HashMap<>();
        details.put("channelId", channelId);
        details.put("resource", "Channel");
        return details;
    }
}
