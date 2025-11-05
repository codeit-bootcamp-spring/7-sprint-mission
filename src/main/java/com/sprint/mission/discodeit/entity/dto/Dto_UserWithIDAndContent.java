package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder
public record Dto_UserWithIDAndContent(
    UUID userId,
    Dto_User dtoUser,
    Optional<Dto_BinaryContent> binaryContent
){
    public Dto_UserWithIDAndContent {
        if (binaryContent == null) {
            binaryContent = Optional.empty();
        }
    }
}
