package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.ServerDtoMapper;
import com.sprint.mission.discodeit.domain.server.ServerRepository;
import com.sprint.mission.discodeit.domain.server.Server;
import com.sprint.mission.discodeit.application.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ServerReadStatusDto;
import com.sprint.mission.discodeit.application.dto.response.ServerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.sprint.mission.discodeit.application.dto.ServerDtoMapper.*;
import static java.util.Comparator.*;

@Service
@RequiredArgsConstructor
public class BasicServerService implements ServerService {


    private final ServerRepository serverRepository;



    @Override
    public void createPublicServer(ServerCreateRequestDto requestDto) {
        Server server = new Server(
                requestDto.serverName(),
                false,
                requestDto.serverLevel(),
                requestDto.members());
        serverRepository.save(server);


    }

    @Override
    public void createPrivateServer(ServerCreateRequestDto requestDto) {
        Server server = new Server(
                requestDto.serverName(),
                true,
                requestDto.serverLevel(),
                requestDto.members());
        serverRepository.save(server);

    }

    @Override
    public ServerReadStatusDto findRecentReadStatus(ServerRequestDto requestDto) {
        Server server = findById(requestDto.serverId());

        return null;
    }

    private Server findById(UUID uuid) {
        return serverRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("서버를 찾을 수 없습니다."));
    }

    @Override
    public List<UUID> findAllByUserId(UUID userId) {
        return serverRepository.findAll()
                .stream()
                .filter(server -> server.getMembers().contains(userId))
                .map(server->server.getId())
                .toList();

    }

    @Override
    public ServerResponseDto updateServer(ServerRequestDto requestDto) {
        Server server = findById(requestDto.serverId());
        server.updatePrivate(requestDto.isPrivate());
        if (requestDto.serverName()!=null){
            server.updateServerName(requestDto.serverName());
        }
        if(requestDto.serverLevel()!=null){
            server.updateServeLevel(requestDto.serverLevel());
        }
        serverRepository.save(server);

        return serverToResponseDto(server);
    }

    @Override
    public void deleteServer(ServerRequestDto requestDto) {
        serverRepository.remove(findById(requestDto.serverId()));
    }






}
