package com.sprint.mission.discodeit.application.facade;

import com.sprint.mission.discodeit.application.BasicServerService;
import com.sprint.mission.discodeit.application.ChannelCreateService;
import com.sprint.mission.discodeit.application.FindAllChannelByUserService;
import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.response.ServerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerFacade {

    private final BasicServerService basicServerService;
    private final ChannelCreateService channelCreateService;
    private final FindAllChannelByUserService findAllChannelByUserService;

    //BasicServerService
    public ServerResponseDto createServer(ServerCreateRequestDto requestDto, boolean isPrivate) { return basicServerService.createServer(requestDto,isPrivate);}
    public ServerResponseDto updateServer(ServerRequestDto requestDto) {return basicServerService.updateServer(requestDto);}
    public void deleteServer(ServerRequestDto requestDto) {basicServerService.deleteServer(requestDto);}
    public ServerResponseDto getServer(ServerRequestDto requestDto) {return basicServerService.getServer(requestDto);}

    //ChannelCreateService
    public ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto) {return channelCreateService.createChannel(requestDto);}

    //FindAllChannelByUserService
    public List<UUID> findAllChannelByUser(UUID serverId, UUID userId){return findAllChannelByUserService.findAllChannelByUser(serverId,userId);}
}
