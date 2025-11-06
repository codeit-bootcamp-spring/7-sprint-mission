package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChannelRequestDto {
    private final ChannelType channelType;
    private final ChannelVisibility channelVisibility;
    private final String channelName;
}
