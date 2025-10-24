package com.sprint.mission.discodeit.channel.application;



import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.presentation.ChannelService;
import com.sprint.mission.discodeit.channel.presentation.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.channel.presentation.dto.request.ChannelRequestDto;
import com.sprint.mission.discodeit.channel.presentation.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.server.application.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


//dto의 변환로직은 dto가 가지고 있으면 되는구나!!!

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
