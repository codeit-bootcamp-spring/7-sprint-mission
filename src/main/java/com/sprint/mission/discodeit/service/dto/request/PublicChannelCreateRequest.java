package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record PublicChannelCreateRequest(
        @NotNull
        String name,
        @NotNull
        String description
) {
}
