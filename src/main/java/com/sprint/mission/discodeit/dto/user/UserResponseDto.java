package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.type.Role;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        BinaryContentResponseDto profile,
        Boolean online,
        Role role,
        Instant createdAt,
        Instant updatedAt
) {
}
