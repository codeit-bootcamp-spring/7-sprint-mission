package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;

import java.util.List;
import java.util.UUID;

public record ChannelDetailInfo(
        ChannelResponseDTO channel,
        UUID ownerId,
        List<UUID> participantIds
) {
}
