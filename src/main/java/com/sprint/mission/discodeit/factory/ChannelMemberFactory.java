package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.channelmember.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMemberFactory {

  private final UserService userService;
  private final ChannelService channelService;

  public ChannelMember create(ReadStatusCreateReq req) {
    User user = userService.findById(req.userId());
    Channel channel = channelService.findById(req.channelId());

    return ChannelMember.create(
        user,
        channel,
        req.role()
    );
  }
}