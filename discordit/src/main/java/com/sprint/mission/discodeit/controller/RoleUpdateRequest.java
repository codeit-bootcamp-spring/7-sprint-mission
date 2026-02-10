package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.enums.Roles;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoleUpdateRequest(
        @NotNull(message = "userId는 필수입니다.")
        UUID userId,

        @NotNull(message = "newRole은 필수입니다.")
        Roles newRole
) {

}
