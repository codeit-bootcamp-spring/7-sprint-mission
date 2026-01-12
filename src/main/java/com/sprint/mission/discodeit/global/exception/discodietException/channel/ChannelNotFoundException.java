package com.sprint.mission.discodeit.global.exception.discodietException.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException(String key, Object value) {
        super(ErrorCode.CHANNEL_NOT_FOUND, key, value);
    }

    public static ChannelNotFoundException byId(UUID channelId) {
        return new ChannelNotFoundException("channelId", channelId);
    }
}
