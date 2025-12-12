package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        @NotNull
        List<UUID> participantIds
) {
}
