package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.readstatus.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadStatusFactory {

  private final UserService userService;
  private final ChannelService channelService;

  public ReadStatus create(ReadStatusCreateReq req) {
    User user = userService.findById(req.userId());
    Channel channel = channelService.findById(req.channelId());

    return ReadStatus.create(
        user,
        channel
    );
  }
}