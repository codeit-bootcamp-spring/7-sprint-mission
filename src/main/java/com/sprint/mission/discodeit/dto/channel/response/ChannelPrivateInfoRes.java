package com.sprint.mission.discodeit.dto.channel.response;

import java.time.Instant;
import java.util.UUID;

public record ChannelPrivateInfoRes(
    UUID channelId,
    String type,
    UUID managerId,
    String name,
    Instant lastMessageTime
) implements ChannelInfoRes {

}
