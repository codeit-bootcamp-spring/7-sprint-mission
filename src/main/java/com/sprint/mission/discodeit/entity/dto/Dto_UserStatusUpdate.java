package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record Dto_UserStatusUpdate(
    @NotNull
    Instant newLastActiveAt) {}
