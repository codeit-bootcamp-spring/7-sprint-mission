package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record Dto_UserStatusUpdate(
    @NotNull
    Instant newLastActiveAt) {}
