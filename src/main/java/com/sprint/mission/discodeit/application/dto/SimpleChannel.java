package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.channel.Channel;

import java.util.UUID;

public record SimpleChannel(
    UUID channelId,
    String channelName
) {

  public static SimpleChannel from(Channel channel) {
    return new SimpleChannel(channel.getId(), channel.getChannelName());
  }
}
