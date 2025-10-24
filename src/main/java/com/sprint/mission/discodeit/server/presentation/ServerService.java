package com.sprint.mission.discodeit.server.presentation;

import com.sprint.mission.discodeit.server.presentation.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.server.presentation.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.server.presentation.dto.response.ServerReadStatusDto;
import com.sprint.mission.discodeit.server.presentation.dto.response.ServerResponseDto;

import java.util.List;
import java.util.UUID;

public interface ServerService {

    void createPublicServer(ServerCreateRequestDto requestDto);

    void createPrivateServer(ServerCreateRequestDto requestDto);

    ServerReadStatusDto findRecentReadStatus(ServerRequestDto requestDto);

    List<UUID> findAllByUserId(UUID userId);

    ServerResponseDto updateServer(ServerRequestDto requestDto);

    void deleteServer(ServerRequestDto requestDto);

}
