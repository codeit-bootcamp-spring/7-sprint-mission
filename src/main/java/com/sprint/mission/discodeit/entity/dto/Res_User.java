package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_User( //all private final
    @NotBlank(message = "userId is mandatory")
    UUID userId,
    Instant createdAt,
    Instant updatedAt,

    String userName,
    //        String password,
    String eMail,
    UUID profiledId
) {
    public static Res_User from(User user) {
        return Res_User.builder()
                        .userId(user.getId())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .userName(user.getUserName())
//                        .password(user.getPassword())
                        .eMail(user.getEMail())
                        .profiledId(user.getProfileId())
                        .build();
    }
}
