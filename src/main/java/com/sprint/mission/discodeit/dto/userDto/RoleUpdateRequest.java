package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.entity.enums.Role;

import java.util.UUID;

public record RoleUpdateRequest(
        UUID userId,
        Role newRole
) {
}
