package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;

import java.util.List;

public record PrivateChannelCreateDto(
        ChannelScope scope,
        ChannelType type,
        List<String> moderatorIds
) {
}
