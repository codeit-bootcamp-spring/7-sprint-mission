package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService {

    private final ChannelRepository channelRepository;


    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {


        Channel channel = new Channel(request.name(), request.description(), false, null);
        channelRepository.save(channel);
        return ChannelDto.from(channel);
    }

    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {

        Channel channel = new Channel("DM", "DM", true, request.participantIds());
        channelRepository.save(channel);
        return ChannelDto.from(channel);
    }


    public ChannelDto updateChannel(String channelId, ChannelUpdateRequest requestDto) {
        Channel channel = getById(channelId);
        if (requestDto.newName() != null) {
            channel.updateChannelName(requestDto.newName());
        }
        channelRepository.save(channel);
        return ChannelDto.from(channel);
    }


    public void deleteChannel(String id) {
        Channel channel = getById(id);
        channelRepository.delete(channel);
    }

    public List<ChannelDto> getAllByUser(String userId) {
        return getAll().stream().filter(channel -> channel.getType() == ChannelType.PUBLIC || channel.getMembers().contains(userId))
                .map(channel -> ChannelDto.from(channel))
                .toList();
    }

    private Channel getById(String channelId) {
        return channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다"));
    }

    private List<Channel> getAll() {
        return channelRepository.findAll();
    }
}
