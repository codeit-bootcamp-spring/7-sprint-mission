package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.facade.mapper.ChannelFacadeMapper;
import com.sprint.mission.discodeit.factory.ChannelMemberFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ChannelMemberService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ChannelCreationFacade {

  private final ChannelService channelService;
  private final ChannelMemberService channelMemberService;
  private final ChannelFacadeMapper channelFacadeMapper;
  private final ChannelMemberFactory channelMemberFactory;

  //공개 채널 추가
  public ChannelInfoRes createPublicChannel(@NonNull UUID managerId,
      @NonNull ChannelCreateReq req) {
    Channel channel = channelService.create(req);
    channelMemberService.create(
        channelMemberFactory.create(managerId, channel.getId(), ChannelMemberRole.MANAGER));
    return channelFacadeMapper.toInfoRes(channel);
  }

  //비밀 채널 추가
  public ChannelInfoRes createPrivateChannel(@NonNull UUID managerId,
      @NonNull ChannelCreateSecReq req) {
    Channel channel = channelService.create(req);
    channelMemberService.create(
        channelMemberFactory.create(
            managerId,
            channel.getId(),
            ChannelMemberRole.MANAGER));
    req.userIds().forEach(userId -> channelMemberService.create(
        channelMemberFactory.create(
            userId,
            channel.getId(),
            ChannelMemberRole.MEMBER
        ))
    );
    return channelFacadeMapper.toInfoRes(channel);
  }
}
