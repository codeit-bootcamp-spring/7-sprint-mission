package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends DiscodeitException {
    public ReadStatusNotFoundException(User user, Channel channel) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails(user.getId(), channel.getId()));
    }

    public ReadStatusNotFoundException(ReadStatus readStatus) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails(
                readStatus.getUser().getId(),
                readStatus.getChannel().getId()
        ));
    }

    public ReadStatusNotFoundException(UUID uuid) {
        super(Instant.now(), ErrorCode.NOT_FOUND, createDetails(uuid));
    }

    private static Map<String, Object> createDetails(UUID userId, UUID channelId) {
        Map<String, Object> details = new HashMap<>();
        details.put("userId", userId);
        details.put("channelId", channelId);
        details.put("resource", "ReadStatus");
        return details;
    }

    private static Map<String, Object> createDetails(UUID readStatusId) {
        Map<String, Object> details = new HashMap<>();
        details.put("readStatusId", readStatusId);
        details.put("resource", "ReadStatus");
        return details;
    }
}
