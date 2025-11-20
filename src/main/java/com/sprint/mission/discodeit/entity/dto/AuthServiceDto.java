package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AuthServiceDto( //all private final
    @NotNull
    String username,
    @NotNull
    String password
) { }
