package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(
    @NotNull
    UUID userId,
    @NotNull
    Role newRole
) {
}
