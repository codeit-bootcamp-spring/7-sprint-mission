package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.service.dto.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;


    public ChannelResponse createChannel(ChannelCreateRequest request, boolean isPrivate){

        Channel channel = new Channel(request.name(),request.participantIds(), isPrivate);
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
        Channel channel = getById(channelId);
        channelRepository.remove(channel);
    }


    public void addChannelMember(UUID channelId, UUID userId) {
        ReadStatus readStatus = new ReadStatus(userId,channelId, Instant.now());
        readStatusRepository.save(readStatus);
    }

    public List<ChannelResponse> getAllByUser(UUID userId){

        return getAll().stream()
                .filter(channel -> channel.getMembers().contains(userId))
                .map(channel -> ChannelResponse.from(channel))
                .toList();
    }

    private Channel getById(UUID channelId) {
        return channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다"));
    }

    private List<Channel> getAll() {
        return channelRepository.findAll();
    }
}
