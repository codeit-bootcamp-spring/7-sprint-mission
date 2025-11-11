package com.sprint.mission.discodeit.dto.login.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record LoginResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        UUID profileId
) {
    public static LoginResponseDto from(User user) {
        return LoginResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreateAt())
                .updatedAt(user.getUpdateAt())
                .username(user.getUsername())
                .password(user.getPassword())
                .profileId(user.getProfileId())
                .build();
    }
}
