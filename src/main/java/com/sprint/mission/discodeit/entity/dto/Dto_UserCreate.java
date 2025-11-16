package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

@Builder
public record Dto_UserCreate( //all private final
        String username,
        String email,
        String password
        ) {}
