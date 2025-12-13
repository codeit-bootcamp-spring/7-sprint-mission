package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChannelUpdateRequest(
        @NotNull
        String newName,
        @NotNull
        String newDescription
) {
}
