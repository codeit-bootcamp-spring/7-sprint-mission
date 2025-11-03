package com.sprint.mission.discodeit.application.dto.request;

import java.util.UUID;


public record ServerRequestDto(
        UUID serverId,
        String serverName,
        Long serverLevel,
        boolean isPrivate
) {
}
