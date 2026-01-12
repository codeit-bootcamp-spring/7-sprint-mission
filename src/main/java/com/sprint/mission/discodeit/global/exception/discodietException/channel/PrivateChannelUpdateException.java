package com.sprint.mission.discodeit.global.exception.discodietException.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class PrivateChannelUpdateException extends ChannelException {
    public PrivateChannelUpdateException(String key, Object value) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED, key, value);
    }

    public static <UUID> PrivateChannelUpdateException notAllowed(UUID channelId) {
        return new PrivateChannelUpdateException("channelId", channelId);
    }
}
