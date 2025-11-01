package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelViewFacade {
    private final ChannelService channelService;
    private final ChannelMapper channelMapper;
    
    //채널 목록 : Public 인 경우 전부, Private 인 경우 자신이 참여한 채널만
    public List<ChannelInfoRes> findAllMyChannels(UUID userId) {
        return channelService.findAllByUserId(userId).stream()
                .map(channelMapper::toInfoRes).toList();
    }

    //채널명으로 찾기
    public ChannelInfoRes findByName(String name) {
        return channelMapper.toInfoRes(channelService.findByName(name));
    }
}
