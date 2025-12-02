package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelFactory {

  public Channel create(ChannelCreateReq req) {
    return Channel.createPublic(
        req.name(),
        req.description()
    );
  }

  public Channel create(ChannelCreateSecReq req) {
    return Channel.createPrivate(
        req.userIds()
    );
  }
}
