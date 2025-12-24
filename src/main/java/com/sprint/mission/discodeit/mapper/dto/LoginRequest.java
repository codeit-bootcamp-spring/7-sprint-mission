package com.sprint.mission.discodeit.mapper.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest( //all private final
                            @NotBlank
    String username,
                            @NotBlank
    String password
) { }
