package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;


public record Res_UserLogin( //all private final
    //@NotBlank(message = "newUsername is mandatory")
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    //@NotBlank(message = "newUsername is mandatory")
    String username,
    //@NotBlank(message = "eMail is mandatory")
    String email,
    String password,
    UUID profileId
) {

    public static Res_UserLogin from(User user) {
        return new Res_UserLogin(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getProfileId()
        );
    }
}
