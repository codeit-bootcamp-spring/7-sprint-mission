package com.sprint.mission.discodeit.dto.entity.user.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        BinaryContent profile,
        boolean online
) {
    public static UserResponse toDto(User user, BinaryContent profile) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profile,
                user.getOnline()
        );
    }
}
