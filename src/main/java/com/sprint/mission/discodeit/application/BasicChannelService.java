package com.sprint.mission.discodeit.application;



import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.application.dto.request.ChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;

import com.sprint.mission.discodeit.domain.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


import static com.sprint.mission.discodeit.application.dto.ChannelDtoMapper.channelToResponseDto;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;



    public List<String> getAllMessage(UUID channelId){
        Channel channel = getById(channelId);
        return channel.getHistory().stream()
                .map(id-> messageRepository.findById(id).orElse(null).getContent())
                .toList();
    }

    public List<ChannelResponseDto> findAllByServer(UUID serverId) {
        return getByServer(serverId)
                .stream()
                .map(ChannelDtoMapper::channelToResponseDto)
                .toList();
    }


    public ChannelResponseDto updateChannel(ChannelRequestDto requestDto) {
        Channel channel = getById(requestDto.channelId());
        if (requestDto.channelName()!=null){
            channel.updateChannelName(requestDto.channelName());
        }
        channelRepository.save(channel);
        return channelToResponseDto(channel);
    }


    public void deleteAllByServer(UUID serverId) {
        getByServer(serverId)
                .stream()
                .forEach(channelRepository::remove);
    }


    public void deleteChannel(UUID channelId) {
        Channel channel = getById(channelId);
        channelRepository.remove(channel);
    }



    public List<Channel> getByServer(UUID serverId){
        return channelRepository.findAll()
                .stream()
                .filter(c-> c.getServerId().equals(serverId))
                .toList();
    }

    public void addChannelMember(UUID channelId,UUID userId){
        Channel channel = getById( channelId);
        channel.addChannelMember(userId);
        channelRepository.save(channel);
        ReadStatus readStatus = new ReadStatus(userId, channelId);
        readStatusRepository.save(readStatus);
    }

    private Channel getById(UUID channelId){
        return channelRepository.findById(channelId).orElseThrow(()->new NoSuchElementException("해당 채널이 없습니다"));
    }

    private List<Channel> getAll(UUID channelId){
        return channelRepository.findAll();
    }
}
