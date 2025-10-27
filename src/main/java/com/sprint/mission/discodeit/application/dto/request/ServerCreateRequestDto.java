package com.sprint.mission.discodeit.application.dto.request;

import java.util.List;
import java.util.UUID;

public record ServerCreateRequestDto(

        String serverName,
        Long serverLevel,
        List<UUID> members
) {
}
