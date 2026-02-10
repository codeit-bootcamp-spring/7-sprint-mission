package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.enums.Role;

import java.util.UUID;

public record UserRoleUpdateRequest(
        UUID userId,
        Role newRole
) {
}
