package com.sprint.mission.discodeit.application.dto.request;

public record ChannelUpdateRequest(
        String newName,
        String newDescription
) {
}
