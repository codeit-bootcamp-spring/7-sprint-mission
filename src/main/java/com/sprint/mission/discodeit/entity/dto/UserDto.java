package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String username,
//        String password,
        String email,
        UUID profileId,

        Boolean online
){
    public static UserDto from(User user, boolean isOnline) {
        return UserDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUserName())
//                .password(user.getPassword()) // 패스워드 정보는 제외하세요.
                .email(user.getEMail())
                .profileId(user.getProfileId())
                .online(isOnline)
                .build();
    }
}