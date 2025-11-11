package com.sprint.mission.discodeit.dto.request.channel;

public record ChannelUpdateRequestDto(
        String newName,
        String newDescription,
        Integer slowModeSeconds) {
}
