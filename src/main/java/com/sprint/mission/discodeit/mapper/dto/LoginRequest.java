package com.sprint.mission.discodeit.mapper.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest( //all private final
                            @NotNull
    String username,
                            @NotNull
    String password
) { }
