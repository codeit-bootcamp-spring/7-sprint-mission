package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCreateRequest( //all private final
    @NotBlank
    String username,
    @NotBlank
    String email,
    @NotBlank
    String password
) {}
