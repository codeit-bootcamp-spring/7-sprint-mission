package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;

import java.util.List;
import java.util.Set;

public record ChannelCreateDto(
        String channelName,
        ChannelScope scope,
        ChannelType type,
        String description,
        List<String> moderatorIds
) {
}
