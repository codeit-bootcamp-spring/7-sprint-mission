package com.sprint.mission.discodeit.server.presentation.dto.request;

import java.util.List;
import java.util.UUID;

public record ServerCreateRequestDto(

        String serverName,
        Long serverLevel,
        List<UUID> members
) {
}
