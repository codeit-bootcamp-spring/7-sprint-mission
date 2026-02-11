package com.sprint.mission.discodeit.dto.request.auth;

import com.sprint.mission.discodeit.entity.UserRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserRoleUpdateRequestDto(
        @NotNull
        UUID userId,

        @NotNull
        UserRole newRole
) {
}
