package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


public record LoginRequest(
    @NotEmpty
    String username,
    @NotEmpty
    String password
){}
