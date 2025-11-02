package com.sprint.mission.discodeit.application;



import com.sprint.mission.discodeit.application.dto.ChannelDtoMapper;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.sprint.mission.discodeit.application.ChannelFindHelper.*;
import static com.sprint.mission.discodeit.application.dto.ChannelDtoMapper.channelToResponseDto;


@Service
@RequiredArgsConstructor
public class BasicChannelService {

    private final ChannelRepository channelRepository;




    public List<ChannelResponseDto> findAllByServer(UUID serverId) {
        return findByServer(channelRepository, serverId)
                .stream()
                .map(ChannelDtoMapper::channelToResponseDto)
                .toList();
    }


    public ChannelResponseDto updateChannel(ChannelRequestDto requestDto) {
        Channel channel = findById(channelRepository, requestDto.channelId());
        if (requestDto.channelName()!=null){
            channel.updateChannelName(requestDto.channelName());
        }
        channelRepository.save(channel);
        return channelToResponseDto(channel);
    }


    public void deleteAllByServer(UUID serverId) {
        findByServer(channelRepository,serverId)
                .stream()
                .forEach(channelRepository::remove);
    }


    public void deleteChannel(UUID channelId) {
        Channel channel = findById(channelRepository, channelId);
        channelRepository.remove(channel);
    }


}
