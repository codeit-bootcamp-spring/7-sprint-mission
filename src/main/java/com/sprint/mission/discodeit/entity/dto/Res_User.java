package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Res_User( //all private final
    //@NotBlank(message = "id is mandatory")
    UUID id,
    Instant createdAt,
    Instant updatedAt,

    String username,
    String email,
    String password,
    UUID profileId
) {
    public static Res_User from(User user) {
        return new Res_User(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUserName(),
            user.getEMail(),
            user.getPassword(),
            user.getProfileId()
        );
    }
}
