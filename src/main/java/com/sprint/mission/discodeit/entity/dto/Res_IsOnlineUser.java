package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Res_IsOnlineUser(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String userName,
        String password,
        String eMail,
        UUID profiledId,

        boolean isOnline
){
    public static Res_IsOnlineUser from(User user, boolean isOnline) {
//         Res_User.from(user, user.getProfileId());
        return Res_IsOnlineUser.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .userName(user.getUserName())
//                .password(user.getPassword()) // 패스워드 정보는 제외하세요.
                .eMail(user.getEMail())
                .profiledId(user.getProfileId())
                .isOnline(isOnline)
                .build();
    }
}