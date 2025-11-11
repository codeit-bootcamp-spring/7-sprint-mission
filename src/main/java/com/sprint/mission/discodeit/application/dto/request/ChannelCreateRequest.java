package com.sprint.mission.discodeit.application.dto.request;

import java.util.List;
import java.util.UUID;

public record ChannelCreateRequest(
        String name,
        List<UUID> participantIds,
        String description
) {
}
