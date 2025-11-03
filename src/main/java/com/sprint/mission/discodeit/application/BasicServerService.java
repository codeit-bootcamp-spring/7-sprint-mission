package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.response.ServerResponseDto;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


import static com.sprint.mission.discodeit.application.dto.ChannelDtoMapper.channelToResponseDto;
import static com.sprint.mission.discodeit.application.dto.ServerDtoMapper.serverToResponseDto;

@Service
@RequiredArgsConstructor
public class BasicServerService {


    private final ServerRepository serverRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ServerResponseDto createServer(ServerCreateRequestDto requestDto, boolean isPrivate) {
        Server server = new Server(
                requestDto.serverName(),
                isPrivate,
                requestDto.serverLevel(),
                requestDto.members());
        serverRepository.save(server);
        return serverToResponseDto(server);
    }


    public ServerResponseDto updateServer(ServerRequestDto requestDto) {
        Server server = findById(requestDto.serverId());
        server.updatePrivate(requestDto.isPrivate());
        if (requestDto.serverName() != null) {
            server.updateServerName(requestDto.serverName());
        }
        if (requestDto.serverLevel() != null) {
            server.updateServeLevel(requestDto.serverLevel());
        }
        serverRepository.save(server);
        return serverToResponseDto(server);
    }


    public void deleteServer(ServerRequestDto requestDto) {
        serverRepository.remove(findById(requestDto.serverId()));
    }

    public ServerResponseDto getServer(ServerRequestDto requestDto) {
        Server server = findById(requestDto.serverId());
        return serverToResponseDto(server);
    }

    public Server findById(UUID id) {
        return serverRepository.findById(id).orElseThrow(() -> new NoSuchElementException("서버를 찾을 수 없습니다"));
    }

    public List<UUID> findAllByUserId(UUID userId) {
        return serverRepository.findAll()
                .stream()
                .filter(server -> server.getMembers().contains(userId))
                .map(server->server.getId())
                .toList();
    }

    public void addMember(UUID userId, UUID serverId){
        Server server = findById(serverId);
        User user = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저 없음"));
        server.addMember(user);
    }

    public void addMembers(List<UUID> usersId, UUID serverId){
        Server server = findById(serverId);
        List<User> list = usersId.stream().map(id -> userRepository.findById(id).orElseGet(null)).toList();
        list.forEach(user -> server.addMember(user));
    }

    public ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto) {
        Server server = findById(requestDto.serverId());
        Channel channel = new Channel(requestDto.channelName(), requestDto.serverId(),requestDto.membersId(), requestDto.isPrivate());
        channelRepository.save(channel);
        server.makeChannel(channel);
        serverRepository.save(server);
        return channelToResponseDto(channel);
    }

    public List<UUID> findAllChannelByUser(UUID serverId, UUID userId){
        Server server = findById(serverId);
        List<UUID> list = server.getChannels().stream()
                .map(channelId -> channelRepository.findById(channelId).orElseThrow(()->new IllegalArgumentException("채널이 존재하지 않습니다.")))
                .filter(channel -> channel.getChannelMember().contains(userId))
                .map(channel -> channel.getId())
                .toList();
        return list;

    }
}
