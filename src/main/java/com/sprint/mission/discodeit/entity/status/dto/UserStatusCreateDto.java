package com.sprint.mission.discodeit.entity.status.dto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record UserStatusCreateDto(@NonNull UUID userId) {
}
