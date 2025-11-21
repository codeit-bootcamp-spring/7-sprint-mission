package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entityElement.ChannelType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record ChannelDto(
        UUID id,
        ChannelType type,
        String name,
        String description,
        List<UserDto> participants,
        Instant lastMessageAt
) {
}
