package com.sprint.mission.discodeit.application.dto.response;

import com.sprint.mission.discodeit.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class UserDto {
    UUID id;
    Instant createdAt;
    Instant updatedAt;
    String username;
    String email;
    UUID profileId;
    Boolean online;

    public static UserDto from(User user){
        return UserDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileId(user.getProfile())
                .online(user.checkOnline())
                .build();
    }


}

