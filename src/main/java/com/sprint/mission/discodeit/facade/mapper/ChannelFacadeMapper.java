package com.sprint.mission.discodeit.facade.mapper;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelMemberService;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 채널의 공개 여부에 따라 ResDTO를 맞게 변환해주는 클래스
@Component
@RequiredArgsConstructor
public class ChannelFacadeMapper {

  private final MessageService messageService;
  private final ChannelMemberService channelMemberService;

  public ChannelInfoRes toInfoRes(@NonNull Channel channel) {
    Instant lastMessageTime = messageService.getListMessageTime(channel.getId());
    User manager = channelMemberService.findManagerByChannelId(channel.getId()).getUser();
    List<UUID> memberIds = channelMemberService.findMembersByChannelId(channel.getId()).stream()
        .map(cm -> cm.getUser().getId()).toList();

    return switch (channel.getPublicType()) {
      case PUBLIC -> ChannelMapper.toResDto(
          channel, manager.getId(), lastMessageTime
      );
      case PRIVATE -> ChannelMapper.toResDto(
          channel, manager.getId(), memberIds, lastMessageTime);
      default -> throw new CustomException(ErrorCode.INVALID_CHANNEL_TYPE);
    };
  }
}
