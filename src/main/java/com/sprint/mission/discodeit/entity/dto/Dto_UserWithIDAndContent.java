package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_UserWithIDAndContent(
    UUID userId,
    Dto_User dtoUser
){
}
