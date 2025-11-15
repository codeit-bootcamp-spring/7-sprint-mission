package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.UUID;

public record ChannelPublicInfoRes(
    UUID channelId,
    String type,
    String name,
    String description,
    Instant lastMessageTime
) implements ChannelInfoRes {

  public static ChannelPublicInfoRes from(Channel channel, Instant lastMessageTime) {
    return new ChannelPublicInfoRes(
        channel.getId(),
        channel.getPublicType().getValue(),
        channel.getName(),
        channel.getDescription(),
        lastMessageTime
    );
  }
}
