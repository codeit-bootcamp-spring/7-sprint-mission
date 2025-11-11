package com.sprint.mission.discodeit.dto.channel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePublicChannelRequestDto {
    private final String name;
    private final String description;
}
