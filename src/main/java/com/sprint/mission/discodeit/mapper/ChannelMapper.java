package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  public static ChannelInfoRes toPrivateResDto(
      Channel channel,
      UUID managerId,
      Instant lastMessageTime) {
    return new ChannelPrivateInfoRes(
        channel.getId(),
        channel.getPublicType().getValue(),
        managerId,
        channel.getName(),
        lastMessageTime
    );
  }

  public static ChannelInfoRes toPublicResDto(
      Channel channel,
      UUID managerId,
      Instant lastMessageTime) {
    return new ChannelPublicInfoRes(
        channel.getId(),
        channel.getPublicType().getValue(),
        managerId,
        channel.getName(),
        channel.getDescription(),
        lastMessageTime
    );
  }

  public static ChannelInfoRes toResDto(
      ChannelInfoQuery channel) {
    return new ChannelPublicInfoRes(
        channel.getChannelId(),
        channel.getPublicType().getValue(),
        channel.getManagerId(),
        channel.getName(),
        channel.getDescription(),
        channel.getLastMessageTime()
    );
  }
}
