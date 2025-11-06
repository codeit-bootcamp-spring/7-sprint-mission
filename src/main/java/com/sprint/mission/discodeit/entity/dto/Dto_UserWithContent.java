package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Optional;

@Builder
public record Dto_UserWithContent( //all private final
    @NotBlank(message = "dtoUser is mandatory")
   Dto_User dtoUser,
   Optional<Dto_BinaryContent> binaryContent
){
    public Dto_UserWithContent {
        if (binaryContent == null) {
            binaryContent = Optional.empty();
        }
    }
}
