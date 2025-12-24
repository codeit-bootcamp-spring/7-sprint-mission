package com.sprint.mission.discodeit.common.exception.channel;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelMemberNotFoundException extends ChannelException {
    public ChannelMemberNotFoundException(UUID channelId, UUID userId) {
        super(ErrorCode.CHANNEL_MEMBER_NOT_FOUND, Map.of("channelId", channelId, "userId", userId));
    }
}
