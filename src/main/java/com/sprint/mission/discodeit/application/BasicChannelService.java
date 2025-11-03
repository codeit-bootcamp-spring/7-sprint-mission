package com.sprint.mission.discodeit.application;



import com.sprint.mission.discodeit.application.dto.ChannelDtoMapper;
import com.sprint.mission.discodeit.application.dto.request.MessageForm;
import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;


import static com.sprint.mission.discodeit.application.dto.ChannelDtoMapper.channelToResponseDto;


@Service
@RequiredArgsConstructor
public class BasicChannelService {

    private final ChannelRepository channelRepository;
    private final FileService fileService;


    public List<ChannelResponseDto> findAllByServer(UUID serverId) {
        return findByServer(serverId)
                .stream()
                .map(ChannelDtoMapper::channelToResponseDto)
                .toList();
    }


    public ChannelResponseDto updateChannel(ChannelRequestDto requestDto) {
        Channel channel = findById(requestDto.channelId());
        if (requestDto.channelName()!=null){
            channel.updateChannelName(requestDto.channelName());
        }
        channelRepository.save(channel);
        return channelToResponseDto(channel);
    }


    public void deleteAllByServer(UUID serverId) {
        findByServer(serverId)
                .stream()
                .forEach(channelRepository::remove);
    }


    public void deleteChannel(UUID channelId) {
        Channel channel = findById(channelId);
        channelRepository.remove(channel);
    }

    public Channel findById(UUID channelId){
        return channelRepository.findById(channelId).orElseThrow(()->new NoSuchElementException("해당 채널이 없습니다"));
    }

    public List<Channel> findAll(UUID channelId){
        return channelRepository.findAll();
    }

    public List<Channel> findByServer(UUID serverId){
        return channelRepository.findAll()
                .stream()
                .filter(c-> c.getServerId().equals(serverId))
                .toList();
    }

    public void addChannelMember(UUID channelId,UUID userId){
        Channel channel = findById( channelId);
        channel.addChannelMember(userId);
        channelRepository.save(channel);
    }

    public long getMemberReadStatus(UUID channelId,UUID memberId){
        Channel channel = findById(channelId);
        ChannelMember channelMember = channel.getMembers().stream().filter(cm -> cm.getMemberId().equals(memberId))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당 유저가 채널에 없습니다. "));
        ReadStatus readStatus = channelMember.getReadStatus();
        Instant lastReadAt = readStatus.getLastReadAt();
        return Duration.between(lastReadAt, Instant.now()).toMinutes();
    }

    public void sendMessage(MessageForm form) throws IOException {
        Message message;
        if(form.image().isEmpty()){
            message = new Message(form.userId(), form.content(), form.channelId(), null);
        } else {
            BinaryContent content = fileService.saveMessageFile(form.userId(), form.image());
            message = new Message(form.userId(), form.content(), form.channelId(), content);
        }
        Channel channel = findById(form.channelId());
        channel.sendMessage(message);
        channelRepository.save(channel);
    }
}
