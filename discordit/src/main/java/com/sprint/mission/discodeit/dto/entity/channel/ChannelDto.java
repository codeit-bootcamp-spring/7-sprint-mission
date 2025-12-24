package com.sprint.mission.discodeit.dto.entity.channel;

import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.common.enums.ChannelScope;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        ChannelScope type,
        String name,
        String description,
        List<UserDto> participants,
        Instant lastMessageAt
) {
}
