package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelDetailViewFacade {

  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  //채널명으로 찾기
  public ChannelInfoRes findByName(@NonNull String name) {
    if (channelService.findByName(name) == null) {
      return null;
    }
    return channelMapper.toInfoRes(channelService.findByName(name));
  }
}
