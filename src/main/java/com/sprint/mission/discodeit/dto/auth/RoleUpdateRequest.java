package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.type.Role;

import java.util.UUID;

public record RoleUpdateRequest(
        UUID userId,
        Role newRole
) {
}
