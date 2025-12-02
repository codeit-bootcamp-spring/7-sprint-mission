package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.query.QueryChannelService;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelOverViewFacade {

  private final QueryChannelService queryChannelService;

  //채널 목록 : Public 인 경우 전부, Private 인 경우 자신이 참여한 채널만
  public Map<ChannelType, List<ChannelInfoRes>> findAllMyChannels(UUID userId,
      String searchTxt) {
    String normalizedSearch = (searchTxt == null || searchTxt.trim().isEmpty()) ? "" : searchTxt;

    return queryChannelService.getAllByUser(userId, normalizedSearch).stream()
        .collect(Collectors.groupingBy(
            ChannelInfoQuery::getPublicType,
            Collectors.mapping(
                ChannelMapper::toResDto,
                Collectors.toList()
            )
        ));
  }
}
