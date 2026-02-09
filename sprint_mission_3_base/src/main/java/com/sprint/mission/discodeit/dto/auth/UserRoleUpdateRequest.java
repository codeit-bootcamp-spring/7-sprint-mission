package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.security.UserRole;
import java.util.UUID;

public record UserRoleUpdateRequest(
        UUID userId,
        UserRole role
) {}
