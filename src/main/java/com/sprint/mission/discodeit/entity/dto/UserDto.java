package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserDto( //all private final
    @NotBlank(message = "userId is mandatory")
    UUID userId,
    Instant createdAt,
    Instant updatedAt,

    @NotBlank(message = "username is mandatory")
    String username,
    //        String password,
    @NotBlank(message = "eMail is mandatory")
    String email,

    @NotBlank(message = "profileId is mandatory")
    UUID profileId,

    @NotBlank(message = "online is mandatory")
    Boolean online
){
    public static UserDto from(User user, boolean isOnline) {
        return UserDto.builder()
                .userId(user.getId())
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