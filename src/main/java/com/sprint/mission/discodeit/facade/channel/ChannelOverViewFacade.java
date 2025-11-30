package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.facade.mapper.ChannelFacadeMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelOverViewFacade {

  private final ChannelService channelService;
  private final ChannelFacadeMapper channelFacadeMapper;

  //채널 목록 : Public 인 경우 전부, Private 인 경우 자신이 참여한 채널만
  public Map<ChannelType, List<ChannelInfoRes>> findAllMyChannels(@NonNull UUID userId,
      String searchTxt) {
    boolean hasSearch = (searchTxt != null) && (!searchTxt.trim().isEmpty());

    return channelService.findAllByUserId(userId).entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream()
                .filter(channel -> !hasSearch || channel.getName().contains(searchTxt))
                .map(channelFacadeMapper::toInfoRes).toList()
        ));
  }
}
