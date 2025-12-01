package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        String name,
        String description,
        Integer slowModeSeconds,
        Instant lastMessageAt,
        boolean isPrivate,
        List<UserResponseDto> participants,
        ChannelType type) {

}
