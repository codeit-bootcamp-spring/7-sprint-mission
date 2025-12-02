package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.query.QueryChannelService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelUpdateFacade {

  private final QueryChannelService queryChannelService;
  private final ChannelService channelService;

  public ChannelInfoRes update(UUID id, ChannelUpdateReq req) {
    channelService.update(id, req);
    return ChannelMapper.toResDto(queryChannelService.get(id));
  }
}
