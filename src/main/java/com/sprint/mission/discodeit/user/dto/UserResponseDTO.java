package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.state.UserStatus;
import com.sprint.mission.discodeit.user.state.UserStatusDTO;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID userId,

        String email,

        String nickname,

        String phoneNum,

        Instant createdAt,

        Instant updatedAt
) {
    public static UserResponseDTO fromEntity(User User) {
        return new UserResponseDTO(
                User.getId(),
                User.getEmail(),
                User.getNickname(),
                User.getPhoneNum(),
                User.getCreatedAt(),
                User.getUpdatedAt()
        );
    }
}
