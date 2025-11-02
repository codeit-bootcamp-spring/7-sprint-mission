package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Res_UserLogin(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String userName,
        String password,
        String eMail,
        UUID profiledId) {

    public static Res_UserLogin from(User user) {
        return Res_UserLogin.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .userName(user.getUserName())
                .password(user.getPassword())
                .eMail(user.getEMail())
                .profiledId(user.getProfileId())
                .build();
    }
}
