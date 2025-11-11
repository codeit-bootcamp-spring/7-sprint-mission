package com.sprint.mission.discodeit.dto.channel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePublicChannelRequestDto {
    private final String newName;
    private final String newDescription;
}
