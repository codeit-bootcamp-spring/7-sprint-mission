package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.Optional;

@Builder
public record Dto_UserWithContent(

   Dto_User dtoUser,
   Optional<Dto_BinaryContent> binaryContent
){
    public Dto_UserWithContent {
        if (binaryContent == null) {
            binaryContent = Optional.empty();
        }
    }

}
