package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;


@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_User(
        String username,
        String password,
        String email
        ) {
    public static Dto_User from(String userName, String password, String eMail) {
        return Dto_User.builder()
                                .username(userName)
                                .password(password)
                                .email(eMail)
                                .build();
    }

//    public static Dto_User fromUser(User user) {
//        return Dto_User.builder()
////                .readStatusID(user.getId())
////                .createdAt(user.getCreatedAt())
////                .updatedAt(user.getUpdatedAt())
//                .username(user.getUserName())
//                .password(user.getPassword())
//                .email(user.getEMail())
//                .profileId(user.getProfileId())
//                .build();
//    }
}
