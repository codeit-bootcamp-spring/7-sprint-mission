package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.service.mapper.ChannelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;


    public ChannelResponse createPublicChannel(PublicChannelCreateRequest request){


        Channel channel = new Channel(request.name(), request.description(), false);
        ChannelEntity channelEntity = channelMapper.toChannelEntity(channel);
        channelRepository.save(channelEntity);
        return ChannelResponse.from(channel);
    }

    public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request){

        Channel channel = new Channel("DM", true, request.participantIds());
        channelRepository.save(channel);
        return ChannelResponse.from(channel);
    }


    public ChannelResponse updateChannel(UUID channelId, ChannelUpdateRequest requestDto) {
        Channel channel = getById(channelId);
        if (requestDto.newName() != null) {
            channel.updateChannelName(requestDto.newName());
        }
        channelRepository.save(channel);
        return ChannelResponse.from(channel);
    }


    public void deleteChannel(UUID channelId) {

        channelRepository.delete(channel);
    }

    public List<ChannelResponse> getAllByUser(UUID userId){
        return getAll().stream().filter(channel -> channel.getType()== ChannelType.PUBLIC||channel.getMembers().contains(userId))
                .map(channel -> ChannelResponse.from(channel))
                .toList();
    }

    private ChannelEntity getById(String channelId) {
        return channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다"));
    }

    private List<Channel> getAll() {
        return channelRepository.findAll();
    }
}
