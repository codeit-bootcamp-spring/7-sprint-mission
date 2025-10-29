package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;

import java.util.Set;

public record PrivateChannelCreateRequestDto(
        ChannelScope scope,
        ChannelType type,
        Set<String> moderatorIds,
        Set<String> memberIds
) {
}
