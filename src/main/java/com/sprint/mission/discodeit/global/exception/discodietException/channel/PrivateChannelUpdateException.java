package com.sprint.mission.discodeit.global.exception.discodietException.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class PrivateChannelUpdateException extends ChannelException {
    public PrivateChannelUpdateException() {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED);
    }

    public static <UUID> PrivateChannelUpdateException notAllowed(UUID channelId) {
        PrivateChannelUpdateException privateChannelUpdateException = new PrivateChannelUpdateException();
        privateChannelUpdateException.updateDetail("channelId", channelId);
        return privateChannelUpdateException;
    }
}
