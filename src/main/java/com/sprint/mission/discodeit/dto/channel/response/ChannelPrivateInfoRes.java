package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelPrivateInfoRes(
    UUID channelId,
    String type,
    UUID managerId,
    List<UUID> userIds,                   //채널 참가자
    Instant lastMessageTime            //가장 최근 메세지의 시간정보
) implements ChannelInfoRes {

  public static ChannelPrivateInfoRes from(Channel channel, Instant lastMessageTime) {
    return new ChannelPrivateInfoRes(
        channel.getId(),
        channel.getPublicType().getValue(),
        channel.getManagerId(),
        channel.getUserIds(),
        lastMessageTime
    );
  }
}
