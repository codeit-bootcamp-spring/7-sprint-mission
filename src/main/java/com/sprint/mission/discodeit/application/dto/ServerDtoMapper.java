package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.application.dto.response.ServerResponseDto;
import com.sprint.mission.discodeit.domain.server.Server;
import org.springframework.stereotype.Service;


public final class ServerDtoMapper {

    private ServerDtoMapper() {
    }

    public static ServerResponseDto serverToResponseDto(Server server){
        return new ServerResponseDto(server.getServerName(),
                server.getServerLevel(),
                server.isPrivate());
    }
}
