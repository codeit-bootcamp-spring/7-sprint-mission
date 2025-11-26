package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UserResponseDto> participants,
    Instant lastMessageAt
) {

}

