package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ReadStatusAlreadyExistsException extends DiscodeitException {
    public ReadStatusAlreadyExistsException(ReadStatus readStatus) {
        super(Instant.now(), ErrorCode.ALREADY_EXISTS, createDetails(readStatus));
    }

    private static Map<String, Object> createDetails(ReadStatus readStatus) {
        Map<String, Object> details = new HashMap<>();
        details.put("channelId", readStatus.getChannel().getId());
        details.put("userId", readStatus.getUser().getId());
        details.put("resource", "ReadStatus");
        return details;
    }
}
