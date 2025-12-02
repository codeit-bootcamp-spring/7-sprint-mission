package com.sprint.mission.discodeit.service.dto.request;

public record PublicChannelUpdateRequest(
        String newName,
        String newDescription
) {
}
