package com.sprint.mission.discodeit.service.dto.request;

public record ChannelUpdateRequest(
        String newName,
        String newDescription
) {
}
