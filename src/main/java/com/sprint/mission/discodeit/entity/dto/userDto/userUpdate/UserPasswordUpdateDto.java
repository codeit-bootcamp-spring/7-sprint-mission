package com.sprint.mission.discodeit.entity.dto.userDto.userUpdate;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;
@Builder
public record UserPasswordUpdateDto(@NonNull UUID userId, @NonNull String newPassword) {
}
