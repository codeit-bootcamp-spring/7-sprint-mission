package com.sprint.mission.discodeit.entity.dto.userStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record UserStatusUpdateDto(@NonNull UUID userStatusId) {
}
