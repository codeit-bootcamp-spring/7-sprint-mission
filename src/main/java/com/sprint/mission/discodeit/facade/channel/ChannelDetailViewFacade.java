package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelDetailViewFacade {
    private final ChannelService channelService;
    private final ChannelMapper channelMapper;

    //채널명으로 찾기
    public ChannelInfoRes findByName(String name) {
        return channelMapper.toInfoRes(channelService.findByName(name));
    }
}
