package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserDto( //all private final
    //@NotBlank(message = "id is mandatory")
    UUID id,
    Instant createdAt,
    Instant updatedAt,

    //@NotBlank(message = "newUsername is mandatory")
    String username,
    //        String newPassword,
    //@NotBlank(message = "eMail is mandatory")
    String email,

    //@NotBlank(message = "profileId is mandatory")
    UUID profileId,

    //@NotBlank(message = "online is mandatory")
    Boolean online
){
    public static UserDto from(User user, boolean isOnline) {
        return new UserDto(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUserName(),
            user.getEMail(),
            user.getProfileId(),
            isOnline
        );
    }
}