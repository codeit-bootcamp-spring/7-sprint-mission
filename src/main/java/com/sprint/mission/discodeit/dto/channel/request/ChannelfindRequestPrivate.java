package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelfindRequestPrivate(
         UUID bose
        , String chennalName
        , ChannelType channelType
) {
}
