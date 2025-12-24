package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.exception.ErrorCode.PRIVATE_CHANNEL_UPDATE;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class PrivateChannelUpdateException extends ChannelException {

    public PrivateChannelUpdateException(UUID channelId) {

        super(PRIVATE_CHANNEL_UPDATE,
            Map.of("channelId", channelId));
    }
}
