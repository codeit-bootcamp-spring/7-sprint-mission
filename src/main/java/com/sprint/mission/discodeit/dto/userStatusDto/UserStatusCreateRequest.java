package com.sprint.mission.discodeit.dto.userStatusDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserStatusCreateRequest(@NotNull UUID userId) {
}
