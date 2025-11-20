package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Builder

public record LoginResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        UUID profileId
) {

}
