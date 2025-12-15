package com.sprint.mission.discodeit.global.exception.discodietException.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException() {
        super(ErrorCode.CHANNEL_NOT_FOUND);
    }

    public static ChannelNotFoundException byId(UUID channelId) {
        ChannelNotFoundException channelNotFoundException = new ChannelNotFoundException();
        channelNotFoundException.updateDetail("channelId", channelId);
        return channelNotFoundException;
    }
}
