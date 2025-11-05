package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_User(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String userName,
        String password,
        String eMail,
        UUID profiledId
) {
    public static Res_User from(User user) {
        return Res_User.builder()
                        .id(user.getId())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .userName(user.getUserName())
                        .password(user.getPassword())
                        .eMail(user.getEMail())
                        .profiledId(user.getProfileId())
                        .build();
    }

//    public static Res_User from(Dto_User dto_User, UUID profileId) {
//        return Res_User.builder()
//                .readStatusID(dto_User.readStatusID())
//                .createdAt(dto_User.createdAt())
//                .updatedAt(dto_User.updatedAt())
//                .userName(dto_User.userName())
//                .password(dto_User.password())
//                .eMail(dto_User.eMail())
//                .profiledId(profileId)
//                .build();
//    }
//
//    public static Res_User from(String userName, String password, String eMail, UUID profiledId) {
//        return Res_User.builder()
//                .readStatusID(UUID.randomUUID())
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .userName(userName)
//                .password(password)
//                .eMail(eMail)
//                .profiledId(profiledId)
//                .build();
//    }
}
