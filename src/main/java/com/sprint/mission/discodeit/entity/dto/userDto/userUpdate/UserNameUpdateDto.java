package com.sprint.mission.discodeit.entity.dto.userDto.userUpdate;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;
@Builder
public record UserNameUpdateDto(@NonNull UUID userId, @NonNull String newUserName) {
}
