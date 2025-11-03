package com.sprint.mission.discodeit.entity.dto.userDto.userUpdate;

import com.sprint.mission.discodeit.entity.entityType.UserState;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record UserStateUpdateDto(@NonNull UUID userId, @NonNull UserState newState) {
}
