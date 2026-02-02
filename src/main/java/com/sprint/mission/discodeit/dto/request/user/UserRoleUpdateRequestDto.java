package com.sprint.mission.discodeit.dto.request.user;

import com.sprint.mission.discodeit.security.Role;

import java.util.UUID;

public record UserRoleUpdateRequestDto(
        UUID userId,
        Role newRole
) {
}
