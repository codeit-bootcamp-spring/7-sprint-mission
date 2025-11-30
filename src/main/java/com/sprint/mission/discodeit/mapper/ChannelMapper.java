package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  public static ChannelInfoRes toResDto(
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
      Channel channel,
      UUID managerId,
      List<UUID> userIds,
      Instant lastMessageTime) {
    return new ChannelPrivateInfoRes(
        channel.getId(),
        channel.getPublicType().getValue(),
        channel.getName(),
        managerId,
        userIds,
        lastMessageTime
    );
  }
}
