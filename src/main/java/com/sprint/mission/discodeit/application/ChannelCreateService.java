package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sprint.mission.discodeit.application.dto.ChannelDtoMapper.channelToResponseDto;

@Service
@RequiredArgsConstructor
public class ChannelCreateService {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;

    public ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto) {
        Server server = ServerFindHelper.findById(serverRepository, requestDto.serverId());
        Channel channel = new Channel(requestDto.channelName(), requestDto.serverId(),requestDto.membersId(), requestDto.isPrivate());
        channelRepository.save(channel);
        server.makeChannel(channel);
        serverRepository.save(server);
        return channelToResponseDto(channel);
    }
}
