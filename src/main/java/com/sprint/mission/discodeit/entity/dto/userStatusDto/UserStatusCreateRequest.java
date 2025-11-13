package com.sprint.mission.discodeit.entity.dto.userStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record UserStatusCreateRequest(@NonNull UUID userId) {
}
