package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.exception.ErrorCode.CHANNEL_NOT_FOUND;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class channelNotFoundException extends ChannelException{

    public channelNotFoundException(UUID channelId) {

        super(CHANNEL_NOT_FOUND,
            Map.of("channelId", channelId));
    }
}
