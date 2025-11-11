package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Res_UserLogin( //all private final
    @NotBlank(message = "userName is mandatory")
    UUID userId,
    Instant createdAt,
    Instant updatedAt,

    @NotBlank(message = "userName is mandatory")
    String userName,
//    @NotBlank(message = "password is mandatory")
//    String password,
    @NotBlank(message = "eMail is mandatory")
    String eMail,
    UUID profiledId) {

    public static Res_UserLogin from(User user) {
        return Res_UserLogin.builder()
                .userId(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .userName(user.getUserName())
//                .password(user.getPassword())
                .eMail(user.getEMail())
                .profiledId(user.getProfileId())
                .build();
    }
}
