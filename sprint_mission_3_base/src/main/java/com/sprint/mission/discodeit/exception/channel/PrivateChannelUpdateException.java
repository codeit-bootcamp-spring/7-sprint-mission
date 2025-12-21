package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {
    public PrivateChannelUpdateException(Object channelKey) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE, Map.of("channelKey", channelKey));
    }
}
