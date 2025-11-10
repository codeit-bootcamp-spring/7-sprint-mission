package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.response.ServerResponseDto;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


import static com.sprint.mission.discodeit.application.dto.ChannelDtoMapper.channelToResponseDto;
import static com.sprint.mission.discodeit.application.dto.ServerDtoMapper.serverToResponseDto;
@Slf4j
@Service
@RequiredArgsConstructor
public class BasicServerService {


    private final ServerRepository serverRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;

    public ServerResponseDto createServer(ServerCreateRequestDto requestDto) {
        Server server = new Server(
                requestDto.serverName(),
                requestDto.isPrivate(),
                requestDto.serverLevel(),
                requestDto.members());
        serverRepository.save(server);
        return serverToResponseDto(server);
    }


    public ServerResponseDto updateServer(ServerRequestDto requestDto) {
        Server server = getById(requestDto.serverId());
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
        getById(requestDto.serverId());
        serverRepository.remove(requestDto.serverId());
    }

    public ServerResponseDto getServer(ServerRequestDto requestDto) {
        Server server = getById(requestDto.serverId());
        return serverToResponseDto(server);
    }

    private Server getById(UUID id) {
        log.info("Server FindById 로직 실행 시작");
        return serverRepository.findById(id).orElseThrow(() -> new NoSuchElementException("서버를 찾을 수 없습니다"));
    }

    public List<String> findAllByUserId(UUID userId) {
        return serverRepository.findAll()
                .stream()
                .filter(server -> server.getMembers().contains(userId))
                .map(server->server.getServerName())
                .toList();
    }

    public void addMember(UUID userId, UUID serverId){
        Server server = getById(serverId);
        User user = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저 없음"));
        server.addMember(user);
    }

    public void addMembers(List<UUID> usersId, UUID serverId){
        Server server = getById(serverId);
        List<User> list = usersId.stream().map(id -> userRepository.findById(id).orElseGet(null)).toList();
        list.forEach(user -> server.addMember(user));
    }

    public ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto) {
        Server server = getById(requestDto.serverId());
        Channel channel = new Channel(requestDto.channelName(), requestDto.serverId(),requestDto.membersId(), requestDto.isPrivate());
        channelRepository.save(channel);
        server.makeChannel(channel);
        serverRepository.save(server);
        for (UUID userId : requestDto.membersId()) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId());
            readStatusRepository.save(readStatus);
        }
        return channelToResponseDto(channel);
    }

    public List<UUID> getAllChannelByUser(UUID serverId, UUID userId){
        Server server = getById(serverId);
        List<UUID> list = server.getChannels().stream()
                .map(channelId -> channelRepository.findById(channelId).orElseThrow(()->new IllegalArgumentException("채널이 존재하지 않습니다.")))
                .filter(channel -> channel.getChannelMember().contains(userId))
                .map(channel -> channel.getId())
                .toList();
        return list;

    }
}
