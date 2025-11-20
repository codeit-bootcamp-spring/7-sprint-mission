package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Dto_UserCreate( //all private final
    @NotNull
    String username,
    @NotNull
    String email,
    @NotNull
    String password
) {}
