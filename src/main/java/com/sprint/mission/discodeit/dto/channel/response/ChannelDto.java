package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.ChannelVisibility;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        ChannelVisibility type,
        String name,
        String description,
        List<UUID> participantIds,
        Instant lastMessageAt
) {}
