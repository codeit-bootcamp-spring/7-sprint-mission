package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.channelmember.request.ChannelMemberCreateReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMemberFactory {

  private final UserService userService;
  private final ChannelService channelService;

  public ChannelMember create(UUID userId, UUID channelId, ChannelMemberRole role) {
    User user = userService.findById(userId);
    Channel channel = channelService.findById(channelId);

    return ChannelMember.create(user, channel, role);
  }

  public ChannelMember create(ChannelMemberCreateReq req) {
    return create(req.userId(), req.channelId(), req.role());
  }
}