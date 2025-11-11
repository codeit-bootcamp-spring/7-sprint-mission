package com.sprint.mission.discodeit.dto.request.channel;

public record ChannelPatchRequestDto(

        String newName,
        String newDescription
) {
}
