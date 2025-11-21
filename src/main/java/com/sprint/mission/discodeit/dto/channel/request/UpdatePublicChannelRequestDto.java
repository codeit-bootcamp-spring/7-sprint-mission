package com.sprint.mission.discodeit.dto.channel.request;

public record UpdatePublicChannelRequestDto(
        String newName,
        String newDescription
) { }
