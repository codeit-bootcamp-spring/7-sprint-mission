package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.factory.ChannelMemberFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ChannerlMemberService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChannelCreationFacade {

  private final UserService userService;
  private final ChannelService channelService;
  private final ChannerlMemberService channerlMemberService;
  private final ChannelMapper channelMapper;
  private final ChannelMemberFactory channelMemberFactory;

  //공개 채널 추가
  public ChannelInfoRes createPublicChannel(@NonNull UUID managerId,
      @NonNull ChannelCreateReq req) {
    return channelMapper.toInfoRes(channelService.create(managerId, req));
  }

  //비밀 채널 추가
  @Transactional
  public ChannelInfoRes createPrivateChannel(@NonNull UUID managerId,
      @NonNull ChannelCreateSecReq req) {
    Channel channel = channelService.create(managerId, req);
    channerlMemberService.create(
        channelMemberFactory.create(
            managerId,
            channel.getId(),
            ChannelMemberRole.MANAGER));
    req.userIds().forEach(userId -> channerlMemberService.create(
        channelMemberFactory.create(
            userId,
            channel.getId(),
            ChannelMemberRole.MEMBER
        ))
    );
    return channelMapper.toInfoRes(channel);
  }
}
