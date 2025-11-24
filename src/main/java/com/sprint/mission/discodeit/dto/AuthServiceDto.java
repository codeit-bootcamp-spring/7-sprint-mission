package com.sprint.mission.discodeit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AuthServiceDto( //all private final
    @NotNull
    String username,
    @NotNull
    String password
) { }
