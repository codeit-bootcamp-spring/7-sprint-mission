package com.sprint.mission.discodeit.dto.entity.user.response;

import com.sprint.mission.discodeit.common.enums.Roles;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        Roles role,
        String username,
        String email,
        BinaryContent profile
) {
    public static UserResponse toDto(User user, BinaryContent profile) {
        return new UserResponse(
                user.getId(),
                user.getRole(),
                user.getUsername(),
                user.getEmail(),
                profile);
    }
}
