package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.facade.mapper.ChannelFacadeMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelUpdateFacade {

  private final ChannelService channelService;
  private final ChannelFacadeMapper channelFacadeMapper;

  public ChannelInfoRes update(UUID id, ChannelUpdateReq req) {
    return channelFacadeMapper.toInfoRes(channelService.update(id, req));
  }
}
