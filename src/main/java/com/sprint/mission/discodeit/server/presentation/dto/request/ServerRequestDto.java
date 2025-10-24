package com.sprint.mission.discodeit.server.presentation.dto.request;

import java.util.UUID;


public record ServerRequestDto(
        UUID serverId,
        String serverName,
        Long serverLevel,
        boolean isPrivate
) {
}
