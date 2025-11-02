package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_User(
        String userName,
        String password,
        String eMail
        ) {
    public static Dto_User from(String userName, String password, String eMail) {
        return Dto_User.builder()
                                .userName(userName)
                                .password(password)
                                .eMail(eMail)
                                .build();
    }

//    public static Dto_User fromUser(User user) {
//        return Dto_User.builder()
////                .id(user.getId())
////                .createdAt(user.getCreatedAt())
////                .updatedAt(user.getUpdatedAt())
//                .userName(user.getUserName())
//                .password(user.getPassword())
//                .eMail(user.getEMail())
//                .profiledId(user.getProfileId())
//                .build();
//    }
}
