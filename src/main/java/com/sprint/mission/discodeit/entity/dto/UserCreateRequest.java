package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCreateRequest( //all private final
    @NotNull
    String username,
    @NotNull
    String email,
    @NotNull
    String password
) {}
