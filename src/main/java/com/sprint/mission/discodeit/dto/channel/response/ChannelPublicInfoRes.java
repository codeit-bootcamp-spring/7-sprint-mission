package com.sprint.mission.discodeit.dto.channel.response;

import java.time.Instant;
import java.util.UUID;

public record ChannelPublicInfoRes(
    UUID channelId,
    String type,
    UUID managerId,
    String name,
    String description,
    Instant lastMessageTime
) implements ChannelInfoRes {

}
