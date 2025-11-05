package com.sprint.mission.discodeit.participation.dto;

import com.sprint.mission.discodeit.config.enums.Role;
import com.sprint.mission.discodeit.participation.Participation;
import com.sprint.mission.discodeit.participation.ParticipationDualKey;

import java.time.Instant;
import java.util.UUID;

public record ParticipationResponseDTO(
        ParticipationDualKey participationDualKey,
        String nickname,
        Role role,
        Instant lastReadAt,
        Instant updateAt,
        Instant createAt
) {
    public static ParticipationResponseDTO from(Participation participation) {
        return new ParticipationResponseDTO(
                participation.getId(),
                participation.getNickname(),
                participation.getRole(),
                participation.getLastReadAt(),
                participation.getUpdatedAt(),
                participation.getCreatedAt()
        );
    }

}
