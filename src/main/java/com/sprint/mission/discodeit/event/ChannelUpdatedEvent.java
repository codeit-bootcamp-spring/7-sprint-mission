package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entityElement.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelUpdatedEvent(
        UUID channelId,
        ChannelType type,
        String eventName,
        List<UserDto> participants
) {
}
