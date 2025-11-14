package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record Dto_UserStatus( //all private final
        //@NotBlank(message = "userStatusId is mandatory")
        UUID userStatusId
) {
    public static Dto_UserStatus from(UUID userStatusID) {
        return new Dto_UserStatus(userStatusID);
    }
}
