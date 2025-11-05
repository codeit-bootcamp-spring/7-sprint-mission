package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.user.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID userId,

        String username,

        String email,

        String nickname,

        String phoneNum,

        Instant createdAt,

        Instant updatedAt
) {
    public static UserResponseDTO fromEntity(User User) {
        return new UserResponseDTO(
                User.getId(),
                User.getUsername(),
                User.getEmail(),
                User.getNickname(),
                User.getPhoneNum(),
                User.getCreatedAt(),
                User.getUpdatedAt()
        );
    }
}
