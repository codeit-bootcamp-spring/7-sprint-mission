package com.sprint.mission.discodeit.participation.dto;

import java.util.UUID;

public record ParticipationRequestDTO(
        UUID channelId,
        UUID userId,
        String nickname
) {
}
