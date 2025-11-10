package com.sprint.mission.discodeit.entity.dto.userDto.userUpdate;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record UserPhoneNumUpdateDto(@NonNull UUID userId, String newPhoneNum) {
}
