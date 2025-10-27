package com.sprint.mission.discodeit.application;



import com.sprint.mission.discodeit.domain.channel.ChannelRepository;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.domain.server.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


//dto의 변환로직을 모아주는 Mapper클래스는 만드는 것도 방법 같다

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;



    @Override
    public ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto) {
        Channel channel = Channel.create(requestDto.channelName(), requestDto.serverId());
        channelRepository.save(channel);
        return channelToResponseDto(channel);
    }


    @Override
    public List<ChannelResponseDto> findAllByServer(UUID serverId) {
        return channelRepository.findAll()
                .stream()
                .filter(c-> c.getServerId().equals(serverId))
                .map(this::channelToResponseDto)
                .toList();

    }

    @Override
    public ChannelResponseDto updateChannel(ChannelRequestDto requestDto) {
        Channel channel = channelRepository.findById(requestDto.channelId()).orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
        if (requestDto.channelName()!=null){
            channel.updateChannelName(requestDto.channelName());
        }
        channelRepository.save(channel);
        return channelToResponseDto(channel);
    }

    @Override
    public void deleteAllByServer(UUID serverId) {
        channelRepository.findAll()
                .stream()
                .filter(c-> c.getServerId().equals(serverId))
                .forEach(channelRepository::remove);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException());
        channelRepository.remove(channel);
    }

    @Override
    public ChannelResponseDto findById(UUID uuid) {
        Channel channel = channelRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        return channelToResponseDto(channel);
    }

    private ChannelResponseDto channelToResponseDto(Channel channel){
        return new ChannelResponseDto(channel.getChannelName(), channel.getServerId(),channel.getHistory(),channel.getId());
    }

}
