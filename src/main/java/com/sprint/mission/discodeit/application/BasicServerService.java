package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ServerResponseDto;
import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sprint.mission.discodeit.application.ServerFindHelper.findById;
import static com.sprint.mission.discodeit.application.dto.ServerDtoMapper.serverToResponseDto;

@Service
@RequiredArgsConstructor
public class BasicServerService {


    private final ServerRepository serverRepository;

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
        Server server = findById(serverRepository, requestDto.serverId());
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
        serverRepository.remove(findById(serverRepository, requestDto.serverId()));
    }

    public ServerResponseDto getServer(ServerRequestDto requestDto) {
        Server server = findById(serverRepository, requestDto.serverId());
        return serverToResponseDto(server);
    }
}
