package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_UserWithIDAndContent( //all private final
    @NotBlank(message = "userId is mandatory")
    UUID userId,
    @NotBlank(message = "dtoUser is mandatory")
    Dto_User dtoUser
){
}
