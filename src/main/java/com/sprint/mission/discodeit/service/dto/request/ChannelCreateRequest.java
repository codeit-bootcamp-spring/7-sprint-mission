package com.sprint.mission.discodeit.service.dto.request;

import java.util.List;
import java.util.UUID;

public record ChannelCreateRequest(
        String name,
        List<UUID> participantIds,
        String description
) {
}
